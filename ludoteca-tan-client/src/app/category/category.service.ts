import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Category } from './models/category.model';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {

  urlEndPoint = 'http://localhost:8080/category';

  constructor(private httpClient: HttpClient) {}

  getCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.urlEndPoint);
  }

  saveCategory(category: Category): Observable<Category> {
    let url = this.urlEndPoint;
    
    if (category.id != null){
        url += '/'+ category.id;
        return this.httpClient.put<Category>(url, category);
    } else{
        return this.httpClient.post<Category>(url, category);
    }
  }

  deleteCategory(idCategory : number): Observable<void> {
    return this.httpClient.delete<void>(this.urlEndPoint + '/' + idCategory);
  }  
}