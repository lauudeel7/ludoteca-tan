import { Component, OnInit, inject, model, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatButtonModule } from '@angular/material/button';

import { LoanService } from '../loan.service';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { Loan } from '../models/loan.model';

@Component({
  selector: 'app-loan-edit',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatFormFieldModule, MatSelectModule,
    MatInputModule, MatDatepickerModule, MatButtonModule
  ],
  providers: [DatePipe],
  templateUrl: './loan-edit.component.html',
  styleUrl: './loan-edit.component.scss'
})
export class LoanEditComponent implements OnInit {
  private readonly loanService = inject(LoanService);
  private readonly gameService = inject(GameService);
  private readonly clientService = inject(ClientService);
  private readonly datePipe = inject(DatePipe);

  protected readonly dialogRef = inject(MatDialogRef<LoanEditComponent>);
  protected readonly data = inject(MAT_DIALOG_DATA);

  protected readonly id = model<number | null>(null);
  selectedClient: any = null;
  selectedGame: any = null;
  startDate: Date | null = null;
  endDate: Date | null = null;

  games: any[] = [];
  clients: any[] = [];
  errorMessage = signal<string>('');

  ngOnInit(): void {
    this.gameService.getGames().subscribe(res => this.games = res ?? []);
    this.clientService.getClients().subscribe(res => this.clients = res ?? []);

    if (this.data?.loan) {
      const loan = this.data.loan;
      this.id.set(loan.id);
      this.selectedClient = loan.client;
      this.selectedGame = loan.game;
      this.startDate = new Date(loan.startDate);
      this.endDate = new Date(loan.endDate);
    }
  }

  onSave() {
    if (!this.startDate || !this.endDate || !this.selectedGame || !this.selectedClient) return;

    if (this.endDate < this.startDate) {
      this.errorMessage.set('La fecha de fin no puede ser anterior a la de inicio');
      return;
    }

    const diffTime = Math.abs(this.endDate.getTime() - this.startDate.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    if (diffDays > 14) {
      this.errorMessage.set('El periodo máximo de préstamo no puede superar los 14 días');
      return;
    }

    const payload: any = {
      id: this.id() || undefined,
      game: { id: this.selectedGame.id },
      client: { id: this.selectedClient.id },
      loanDate: this.datePipe.transform(this.startDate, 'yyyy-MM-dd')!,
      returnDate: this.datePipe.transform(this.endDate, 'yyyy-MM-dd')!
    };

    this.loanService.saveLoan(payload).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => {
        if (err.status === 400) {
          const gameId = this.selectedGame?.id || this.data?.loan?.gameId;
          const clientId = this.selectedClient?.id || this.data?.loan?.clientId;

          const gameName = this.games.find(g => g.id == gameId)?.title || 'seleccionado';
          const clientName = this.clients.find(c => c.id == clientId)?.name || 'seleccionado';

          this.errorMessage.set(
            `No se puede guardar: Comprueba si el juego "${gameName}" ya está prestado en estas fechas, o si el cliente "${clientName}" ya ha alcanzado el límite máximo de 2 préstamos activos.`
          );
        } else {
          this.errorMessage.set('Error de conexión o servidor no disponible.');
        }
      }
    });

  }

  compareObjects(o1: any, o2: any): boolean {
    return o1 && o2 ? Number(o1.id) === Number(o2.id) : o1 === o2;
  }

  onClose() { this.dialogRef.close(false); }
}
