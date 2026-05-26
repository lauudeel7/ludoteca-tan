import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { PaginatedData } from '../core/model/page/PaginatedData';
import { Loan } from './models/loan.model';

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private readonly http = inject(HttpClient);
  private readonly urlEndPoint = 'http://localhost:8080/loan';

  getLoans(pageable: Pageable, gameId?: number, clientId?: number, date?: string): Observable<PaginatedData<Loan>> {
    const searchDto = {
      gameId: gameId || null,
      clientId: clientId || null,
      date: date || null,
      pageable: {
        pageNumber: pageable.pageNumber,
        pageSize: pageable.pageSize,
        sort: {
          empty: true,
          sorted: false,
          unsorted: true
        },
        offset: pageable.pageNumber * pageable.pageSize,
        paged: true,
        unpaged: false
      }
    };

    return this.http.post<PaginatedData<Loan>>(this.urlEndPoint, searchDto);
  }

  saveLoan(loan: Loan): Observable<void> {
    let url = this.urlEndPoint;
    if (loan.id != null) {
      url += '/' + loan.id;
    }
    return this.http.put<void>(url, loan);
  }

  deleteLoan(idLoan: number): Observable<void> {
    return this.http.delete<void>(`${this.urlEndPoint}/${idLoan}`);
  }
}
