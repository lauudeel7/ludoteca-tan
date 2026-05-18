import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from './models/client.model';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly urlEndPoint = 'http://localhost:8080/clients';

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.urlEndPoint);
  }

  saveCategory(client: Client): Observable<Client> {
    let url = this.urlEndPoint;
    if (client.id != null) {
      url += '/' + client.id;
      return this.http.put<Client>(url, client);
    } else {
      return this.http.post<Client>(url, client);
    }
  }

  deleteCategory(idClient: number): Observable<void> {
    return this.http.delete<void>(this.urlEndPoint + '/' + idClient);
  }
}
