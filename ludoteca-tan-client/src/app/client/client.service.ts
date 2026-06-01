import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Client } from './models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:8080/client';

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  saveClient(client: Client): Observable<Client> {
    const { id } = client;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
    return this.http.put<Client>(url, client);
  }

  deleteClient(idClient: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idClient}`);
  }

  checkClientLoans(idClient: number): Observable<boolean> {
    return of(false); 
}
}
