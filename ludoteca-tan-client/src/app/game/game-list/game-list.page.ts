import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Game } from '../models/game.model';
import { GameService } from '../game.service';
import { ChangeDetectorRef } from '@angular/core';
import { GameEditComponent } from '../game-edit/game-edit.component';
import { FormsModule } from '@angular/forms';
import { Category } from '../../category/models/category.model';
import { CategoryService } from '../../category/category.service';
import { GameItemComponent } from './game-item/game-item'; 
import { Author } from '../../author/models/author.model';
import { AuthorService } from '../../author/author.service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    GameEditComponent, 
    GameItemComponent,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './game-list.page.html',
  styleUrls: ['./game-list.page.scss']
})
export class GameListPage implements OnInit {

  games: Game[] = [];
  categories: Category[] = [];
  authors: Author[] = [];
  title: string = "";
  categoryId?: number;
  selectedGame: Game | null = null;
  showModal: boolean = false;

  constructor(
    private gameService: GameService,
    private categoryService: CategoryService,
    private authorService: AuthorService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadAuthors();
    this.loadGames();
    
  }

    loadGames(): void {
    this.gameService.getGames(
      this.title ? this.title : undefined,
      this.categoryId ?? undefined
    ).subscribe(
      games => {
        this.games = games;
        this.mapGamesWithReferences(); 
      }
    );
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      categories => {
        this.categories = categories;
        this.mapGamesWithReferences(); 
      }
    );
  }

  loadAuthors(): void {
    this.authorService.getAllAuthors().subscribe(
      authors => {
        this.authors = authors;
        this.mapGamesWithReferences();
      }
    );
  }


      mapGamesWithReferences(): void {
    if (!this.games || this.games.length === 0) return;

    this.games = this.games.map(game => {
      const catId = (game as any).idCategory || 
                    (game as any).categoryId || 
                    game.category?.id;

      const autId = (game as any).idAuthor || 
                    (game as any).authorId || 
                    game.author?.id;

      const matchingCategory = this.categories.find(c => c.id === catId);
      const matchingAuthor = this.authors.find(a => a.id === autId);

      return {
        ...game,
        categoryName: matchingCategory ? matchingCategory.name : (game.category?.name || 'Sin categoría'),
        authorName: matchingAuthor ? matchingAuthor.name : (game.author?.name || 'Sin autor'),
        authorNationality: matchingAuthor ? matchingAuthor.nationality : (game.author?.nationality || 'Desconocida')
      };
    });

    this.cdr.detectChanges();
  }





  search(): void {
    this.loadGames();
  }

  cleanFilters(): void {
    this.title = "";
    this.categoryId = undefined;
    this.loadGames();
  }

  createGame(): void {
    this.selectedGame = {
      id: undefined,
      title: "",
      age: 0,
      category: undefined,
      author: undefined
    };

    this.showModal = true;
  }

  editGame(game: Game): void {
    this.selectedGame = JSON.parse(JSON.stringify(game));
    this.showModal = true;
  }

  closeModal(): void {
    this.selectedGame = null;
    this.showModal = false;
    this.loadGames();
  }
}
