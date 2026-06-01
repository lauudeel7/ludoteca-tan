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
import { Game } from '../../game/models/game.model';
import { Client } from '../../client/models/client.model';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { Loan } from '../models/loan.model';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-loan-edit',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule 
  ],
  templateUrl: './loan-edit.component.html',
  styleUrls: ['./loan-edit.component.scss']
})
export class LoanEditComponent implements OnInit {
    protected readonly id = signal<number | null>(null);
    protected readonly gameId = signal<number | null>(null);
    protected readonly clientId = signal<number | null>(null);
    protected readonly startDate = signal<Date | null>(null);
    protected readonly endDate = signal<Date | null>(null);
    
    protected readonly games = signal<Game[]>([]);
    protected readonly clients = signal<Client[]>([]);

    protected readonly dialogRef = inject(MatDialogRef<LoanEditComponent>);
    protected readonly data = inject(MAT_DIALOG_DATA);
    protected readonly loanService = inject(LoanService);
    protected readonly gameService = inject(GameService);
    protected readonly clientService = inject(ClientService);

    protected isValid() {
        return this.gameId() !== null && this.clientId() !== null && this.startDate() !== null && this.endDate() !== null;
    }

    ngOnInit(): void {
        this.loadFormData(this.data.loan ?? null);
    }

    loadFormData(initialData: Loan | null): void {
        this.id.set(initialData?.id ?? null);

        this.gameService.getGames().subscribe((gamesList) => {
            this.games.set(gamesList);
            this.gameId.set(initialData?.game?.id ?? null);
        });

        this.clientService.getClients().subscribe((clientsList) => {
            this.clients.set(clientsList ?? []);
            this.clientId.set(initialData?.client?.id ?? null);
        });

        this.startDate.set(initialData?.startDate ? new Date(initialData.startDate) : null);
        this.endDate.set(initialData?.endDate ? new Date(initialData.endDate) : null);
    }

    onSave() {
        const id = this.id();
        const gameId = this.gameId(); 
        const clientId = this.clientId(); 
        const startDate = this.startDate(); 
        const endDate = this.endDate(); 

        const loan = {
            id,
            game: this.games().find(g => g.id === gameId) ?? null,
            client: this.clients().find(c => c.id === clientId) ?? null,
            startDate: startDate ? startDate.toISOString().split('T')[0] : null,
            endDate: endDate ? endDate.toISOString().split('T')[0] : null
        } as unknown as Loan;

        this.loanService.saveLoan(loan).subscribe(() => {
            this.dialogRef.close(true);
        });
    }

    onClose() {
        this.dialogRef.close();
    }
}
