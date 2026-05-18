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
  title: string = "";
  categoryId?: number;
  selectedGame: Game | null = null;
  showModal: boolean = false;

  constructor(
    private gameService: GameService,
    private categoryService: CategoryService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadGames();
    this.loadCategories();
  }

  loadGames(): void {
    this.gameService.getGames(
      this.title ? this.title : undefined,
      this.categoryId ?? undefined
    ).subscribe(
      games => {
        this.games = games;
        this.cdr.detectChanges();
      }
    );
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      categories => {
        this.categories = categories;
      }
    );
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
