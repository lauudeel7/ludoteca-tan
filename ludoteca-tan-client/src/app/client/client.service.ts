import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from './models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {

  urlEndPoint = 'http://localhost:8080/client';

  constructor(private http: HttpClient) {}

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.urlEndPoint);
  }

  saveClient(client: Client): Observable<Client> {
    let url = this.urlEndPoint;
    if (client.id != null) {
      url += '/' + client.id;
      return this.http.put<Client>(url, client);
    } else {
      return this.http.post<Client>(url, client);
    }
  }

  deleteClient(idClient: number): Observable<void> {
    return this.http.delete<void>(this.urlEndPoint + '/' + idClient);
  }

  checkClientLoans(idClient: number): Observable<boolean> {
  return this.http.get<boolean>(`http://localhost:8080/loan/exists-client/${idClient}`);
}
}
