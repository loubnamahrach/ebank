import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpDownloadProgressEvent, HttpEventType } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { MarkdownModule } from 'ngx-markdown';
import { LoadingService } from '../services/loading.service';

@Component({
  selector: 'app-bot-ui',
  standalone: true,
  imports: [FormsModule, AsyncPipe, MarkdownModule],
  templateUrl: './bot-ui.component.html',
  styleUrl: './bot-ui.component.css'
})
export class BotUiComponent {
  query: any;
  http = inject(HttpClient);
  response$!: Observable<any>;
  public loadingService = inject(LoadingService);

  askAgent() {
    this.response$ = this.http
      .get("http://localhost:9999/EBANK-BOT/chat?query=" + encodeURIComponent(this.query ?? ''), { responseType: 'text' })
      .pipe(map(body => ({ type: 'Response', content: body })));
  }

  askAgentStream() {
    this.response$ = this.http
      .get("http://localhost:9999/EBANK-BOT/chatStream?query=" + encodeURIComponent(this.query ?? ''),
        { responseType: 'text', observe: "events", reportProgress: true })
      .pipe(map(event => {
        switch (event.type) {
          case HttpEventType.Sent:
            return { type: 'Sent' };
          case HttpEventType.DownloadProgress:
            return { type: "Response", content: (event as HttpDownloadProgressEvent).partialText };
          case HttpEventType.Response:
            return { type: "Response", content: event.body };
          default:
            return { type: 'Other', data: event };
        }
      }));
  }
}
