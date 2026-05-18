import { Component, EventEmitter, Output, Input, OnInit, OnChanges, SimpleChanges, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GameService } from '../game.service';
import { Game } from '../models/game.model';
import { AuthorService } from '../../author/author.service';
import { Author } from '../../author/models/author.model';
import { CategoryService } from '../../category/category.service';
import { Category } from '../../category/models/category.model';

import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-game-edit',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatSelectModule
  ],
  templateUrl: './game-edit.component.html',
  styleUrls: ['./game-edit.component.scss']
})
export class GameEditComponent implements OnInit, OnChanges {
  @Input()
  game!: Game;

  @Output()
  close = new EventEmitter<void>();

  categories: Category[] = [];
  authors: Author[] = [];

  constructor(
    private categoryService: CategoryService,
    private authorService: AuthorService,
    private gameService: GameService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.initializeNestedObjects();
    this.loadCategories();
    this.loadAuthors();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['game'] && this.game) {
      this.initializeNestedObjects();
      this.syncSelects();
    }
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (res) => {
        this.categories = res ?? [];
        this.syncSelects();
      }
    });
  }

  loadAuthors(): void {
    this.authorService.getAllAuthors().subscribe({
      next: (res) => {
        this.authors = res ?? [];
        this.syncSelects();
      }
    });
  }

  private initializeNestedObjects(): void {
    if (!this.game) return;

    if (!this.game.category) {
      this.game.category = {
        id: (this.game as any).categoryId,
        name: (this.game as any).categoryName || ''
      };
    }

    if (!this.game.author) {
      this.game.author = {
        id: (this.game as any).authorId,
        name: (this.game as any).authorName || '',
        nationality: (this.game as any).authorNationality || ''
      };
    }
  }

  private syncSelects(): void {
    if (!this.game) return;

    if (this.categories.length > 0) {
      const matchedCategory = this.categories.find(c =>
        (this.game.category?.id && Number(c.id) === Number(this.game.category.id)) ||
        (c.name === (this.game as any).categoryName)
      );
      if (matchedCategory) {
        this.game.category = matchedCategory;
      }
    }

    if (this.authors.length > 0) {
      const matchedAuthor = this.authors.find(a =>
        (this.game.author?.id && Number(a.id) === Number(this.game.author.id)) ||
        (a.name === (this.game as any).authorName)
      );
      if (matchedAuthor) {
        this.game.author = matchedAuthor;
      }
    }

    this.cdr.detectChanges();
  }

  compareObjects(o1: any, o2: any): boolean {
    if (!o1 || !o2) return o1 === o2;
    return Number(o1.id) === Number(o2.id);
  }

  isValid(): boolean {
    return !!this.game.title &&
      (this.game.age ?? 0) >= 0 &&
      (this.game.age ?? 0) <= 99 &&
      !!this.game.category?.id &&
      !!this.game.author?.id;
  }

  save(): void {
    if (!this.isValid()) return;

    if (this.game.id) {
      this.gameService.updateGame(this.game.id, this.game).subscribe(() => this.close.emit());
    } else {
      this.gameService.saveGame(this.game).subscribe(() => this.close.emit());
    }
  }

  cancel(): void {
    this.close.emit();
  }
}



