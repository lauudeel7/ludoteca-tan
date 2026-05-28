import { Component, input } from '@angular/core';
import { Game } from '../../models/game.model';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-game-item',
  standalone: true,
  imports: [MatCardModule],
  templateUrl: './game-item.html',
  styleUrls: ['./game-item.scss']
})
export class GameItemComponent {
    public readonly game = input.required<Game>();
}