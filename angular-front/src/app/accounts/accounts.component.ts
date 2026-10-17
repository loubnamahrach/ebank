import { Component, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AsyncPipe } from '@angular/common'; // <-- Important pour | async
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs';
import { Account, AccountListState, RequestStatus } from '../model/account.model';
import {LoadingService} from '../services/loading.service';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [AsyncPipe],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent {
  private http = inject(HttpClient);
  public loadService =inject(LoadingService);
  accounts$: Observable<AccountListState> = this.http.get<Account[]>(
    "http://localhost:9999/EBANK-SERVICE/accounts"
  ).pipe(
    map(resp => {
      return { accounts: resp, status: RequestStatus.SUCCESS };
    }),
    catchError((err, caught) => {
      return of({ status: RequestStatus.ERROR, errorMessage: err.statusText });
    })
  );
}
