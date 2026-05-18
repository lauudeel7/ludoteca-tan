import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Author } from './models/author.model';
import { PaginatedData } from '../core/model/page/PaginatedData';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthorService {
  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:8080/authors';

  getAuthors(pageable: Pageable): Observable<PaginatedData<Author>> {
    const url = `${this.baseUrl}?page=${pageable.pageNumber}&size=${pageable.pageSize}`;
    return this.http.get<PaginatedData<Author>>(url);
  }

  getAllAuthors(): Observable<Author[]> {
    const url = `${this.baseUrl}?page=0&size=1000`;
    
    return this.http.get<PaginatedData<Author>>(url).pipe(
      map(response => response.content ?? [])
    );
  }


  saveAuthor(author: Author): Observable<Author> {
    const { id } = author;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
    return this.http.put<Author>(url, author);
  }

  deleteAuthor(idAuthor: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${idAuthor}`);
  }
}