import { Component, input, output } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-game-item',
  standalone: true,
  imports: [MatCardModule],
  templateUrl: './game-item.html',
  styleUrls: ['./game-item.scss']
})
export class GameItemComponent {
  game = input.required<any>();
  
  cardClick = output<void>();

  onCardClick() {
    this.cardClick.emit();
  }
}