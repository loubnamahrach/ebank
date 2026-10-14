import {Component, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent {
  private http = inject(HttpClient);
  accounts$ : Observable<AccountListState> = this.http.get<Observable<AccountListState>>(
    "http://localhost:9999/EBANK-SERVICE/accounts"
  )
    .pipe(
      map(resp => {
        return {accounts: resp, status: RequestStatus.SUCCESS}
      }),
      catchError((err, caught) => {
        return of({status : RequestStatus.ERROR, errorMessage : err.statusText })
      })
    )
}
