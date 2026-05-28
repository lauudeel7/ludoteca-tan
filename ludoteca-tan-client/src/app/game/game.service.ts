import { Injectable, inject} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, of } from "rxjs";
import { Game } from './models/game.model';

@Injectable({
    providedIn: 'root',
})
export class GameService {
    protected readonly http = inject(HttpClient);

    private baseUrl = 'http://localhost:8080/game';

    getGames(title?: string, categoryId?: number): Observable<Game[]> {
        return this.http.get<Game[]>(this.composeFindUrl(title, categoryId));
    }

    saveGame(game: Game): Observable<void> {
        const { id } = game;
        const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;

        return this.http.put<void>(url, game);
    }

    private composeFindUrl(title?: string, categoryId?: number): string {
        const params = new URLSearchParams();
        if (title) {
          params.set('title', title);
        }  
        if (categoryId) {
            params.set('idCategory', categoryId.toString());
        }
        const queryString = params.toString();
        return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
    }
}