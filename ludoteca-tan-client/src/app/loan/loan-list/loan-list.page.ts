import { Component, OnInit, inject, signal  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { LoanService } from '../loan.service';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { Pageable } from '../../core/model/page/Pageable';
import { Loan } from '../models/loan.model';
import { Game } from '../../game/models/game.model';
import { Client } from '../../client/models/client.model';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatPaginatorModule,
    LoanEditComponent
  ],
  templateUrl: './loan-list.page.html',
  styleUrls: ['./loan-list.page.scss']
})
export class LoanListPage implements OnInit {

    protected readonly loans = signal<Loan[]>([]);
    protected readonly games = signal<Game[]>([]);
    protected readonly clients = signal<Client[]>([]);
    
    protected readonly filterGame = signal<Game | null>(null);
    protected readonly filterClient = signal<Client | null>(null);
    protected readonly filterDate = signal<Date | null>(null);

    protected readonly displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'action'];
    
    protected pageNumber = 0;
    protected pageSize = 5;
    protected totalElements = 0;

    protected readonly loanService = inject(LoanService);
    protected readonly gameService = inject(GameService);
    protected readonly clientService = inject(ClientService);
    protected readonly dialog = inject(MatDialog);

    ngOnInit(): void {
        this.loadPage();
        this.gameService.getGames().subscribe((games) => this.games.set(games));
        this.clientService.getClients().subscribe((clients) => this.clients.set(clients));
    }

    protected loadPage(event?: PageEvent): void {
        if (event) {
            this.pageNumber = event.pageIndex;
            this.pageSize = event.pageSize;
        }

        const pageable: Pageable = {
            pageNumber: this.pageNumber,
            pageSize: this.pageSize,
            sort: []
        };

        const idGame = this.filterGame()?.id ?? undefined;
        const idClient = this.filterClient()?.id ?? undefined;
        const dateStr = this.filterDate() ? this.filterDate()!.toISOString().split('T')[0] : undefined;

        this.loanService
            .getLoans(pageable, idGame, idClient, dateStr)
            .subscribe((page) => {
                this.loans.set(page.content);
                this.totalElements = page.totalElements;
            });
    }

    protected onSearch(): void {
        this.pageNumber = 0;
        this.loadPage();
    }

    protected onCleanFilter(): void {
        this.filterGame.set(null);
        this.filterClient.set(null);
        this.filterDate.set(null);
        this.onSearch();
    }

    protected cleanFilters(): void {
        this.onCleanFilter();
    }

    protected createLoan(): void {
        const dialogRef = this.dialog.open(LoanEditComponent, {
            data: {},
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (!result) return;
            this.onSearch();
        });
    }

    protected editLoan(loan: Loan): void {
        const dialogRef = this.dialog.open(LoanEditComponent, {
            data: { loan: loan },
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (!result) return;
            this.loadPage();
        });
    }

    protected deleteLoan(loan: Loan): void {
        this.loanService.deleteLoan(loan.id).subscribe(() => {
            this.loadPage();
        });
    }
}
