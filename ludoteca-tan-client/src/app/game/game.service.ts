import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Game } from './models/game.model';

@Injectable({
    providedIn: 'root',
})
export class GameService {
    urlEndPoint = 'http://localhost:8080/game';

    constructor(private httpClient: HttpClient) { }

        getGames(
        title?: string,
        categoryId?: number
    ): Observable<Game[]> {

        let params = new HttpParams();

        if (title) {
            params = params.set('title', title);
        }

        if (categoryId !== undefined) {
            params = params.set('idCategory', categoryId);
        }

        return this.httpClient.get<Game[]>(this.urlEndPoint, { params });
    }


    getGame(idGame: number): Observable<Game> {
      return this.httpClient.get<Game>(this.urlEndPoint+'/'+idGame);
    }

    saveGame(game: Game): Observable<Game> {
        
        if(game.id){
            return this.httpClient.put<Game>(
                `${this.urlEndPoint}/${game.id}`,
                game
            );
        }else{
            return this.httpClient.post<Game>(
                this.urlEndPoint,
                game
            );
        }
    }

    updateGame(id: number, game: Game): Observable<Game>{
        return this.httpClient.put<Game>(`${this.urlEndPoint}/${id}`, game);
    }
}