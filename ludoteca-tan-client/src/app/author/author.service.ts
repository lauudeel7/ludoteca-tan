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

  private baseUrl = 'http://localhost:8080/author';

    getAuthors(pageable: Pageable): Observable<PaginatedData<Author>> {
    const searchDto = {
      pageable: {
        pageNumber: pageable.pageNumber,
        pageSize: pageable.pageSize
      }
    };
    return this.http.post<PaginatedData<Author>>(this.baseUrl, searchDto);
  }

  getAllAuthors(): Observable<Author[]> {
    const searchDto = {
      pageable: {
        pageNumber: 0,
        pageSize: 1000
      }
    };
    
    return this.http.post<PaginatedData<Author>>(this.baseUrl, searchDto).pipe(
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