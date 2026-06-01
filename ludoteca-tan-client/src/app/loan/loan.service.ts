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
  protected readonly http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/loan';

  getLoans(pageable: Pageable, idGame?: number, idClient?: number, date?: string): Observable<PaginatedData<Loan>> {
    const searchDto = {
      idGame: idGame || null,
      idClient: idClient || null,
      date: date || null
    };

    return this.http.post<PaginatedData<Loan>>(this.composeFindUrl(pageable), searchDto);
  }

  saveLoan(loan: Loan): Observable<void> {
    const { id } = loan;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;

    return this.http.put<void>(url, loan);
  }

  deleteLoan(idLoan: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${idLoan}`);
  }

   private composeFindUrl(pageable: Pageable): string {
    const params = new URLSearchParams();
    
    if (pageable) {
      params.set('page', pageable.pageNumber.toString());
      params.set('size', pageable.pageSize.toString());
    }
    
    const queryString = params.toString();
    return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
  }
}
