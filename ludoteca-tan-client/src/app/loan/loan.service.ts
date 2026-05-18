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
  private readonly urlEndPoint = 'http://localhost:8080/loans';

  getLoans(pageable: Pageable, gameId?: number, clientId?: number, date?: string): Observable<PaginatedData<Loan>> {
    let paramsUrl = `${this.urlEndPoint}?page=${pageable.pageNumber}&size=${pageable.pageSize}`;
    if (gameId) paramsUrl += `&gameId=${gameId}`;
    if (clientId) paramsUrl += `&clientId=${clientId}`;
    if (date) paramsUrl += `&date=${date}`;
    return this.http.get<PaginatedData<Loan>>(paramsUrl);
  }

  saveLoan(loan: Loan): Observable<Loan> {
    return this.http.post<Loan>(this.urlEndPoint, loan);
  }

  deleteLoan(idLoan: number): Observable<void> {
    return this.http.delete<void>(`${this.urlEndPoint}/${idLoan}`);
  }
}
