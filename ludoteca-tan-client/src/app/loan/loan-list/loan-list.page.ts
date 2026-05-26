import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
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
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { Pageable } from '../../core/model/page/Pageable';
import { Loan } from '../models/loan.model';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [
    CommonModule, FormsModule, MatTableModule, MatPaginatorModule, MatDialogModule,
    MatFormFieldModule, MatSelectModule, MatInputModule, MatDatepickerModule,
    MatNativeDateModule, MatButtonModule, MatIconModule
  ],
  providers: [DatePipe],
  templateUrl: './loan-list.page.html',
  styleUrl: './loan-list.page.scss'
})
export class LoanListPage implements OnInit {
  private readonly loanService = inject(LoanService);
  private readonly gameService = inject(GameService);
  private readonly clientService = inject(ClientService);
  private readonly dialog = inject(MatDialog);
  private readonly datePipe = inject(DatePipe);

  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  filterGameId: number | null = null;
  filterClientId: number | null = null;
  filterDate: Date | null = null;

  games: any[] = [];
  clients: any[] = [];
  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'action'];

  ngOnInit(): void {
    this.gameService.getGames().subscribe(res => this.games = res ?? []);
    this.clientService.getClients().subscribe(res => this.clients = res ?? []);
    this.loadPage();
  }

  loadPage(event?: PageEvent) {
    if (event != null) {
      this.pageSize = event.pageSize;
      this.pageNumber = event.pageIndex;
    }

    const pageable: Pageable = { pageNumber: this.pageNumber, pageSize: this.pageSize, sort: [] };
    const formattedDate = this.filterDate ? this.datePipe.transform(this.filterDate, 'yyyy-MM-dd')! : undefined;

    this.loanService.getLoans(pageable, this.filterGameId || undefined, this.filterClientId || undefined, formattedDate)
      .subscribe(data => {
        this.dataSource.data = data.content ?? [];
        this.totalElements = data.totalElements;
      });
  }

  getGameTitle(gameId: any): string {
  if (!this.games || this.games.length === 0) return 'Cargando juegos...';
  if (!gameId) return 'Sin ID';
  
  console.log('Buscando juego con ID:', gameId, 'dentro de la lista:', this.games);
  
  const game = this.games.find(g => g.id == gameId);
  return game ? game.title : `ID ${gameId} no encontrado`;
}

getClientName(clientId: any): string {
  if (!this.clients || this.clients.length === 0) return 'Cargando clientes...';
  if (!clientId) return 'Sin ID';
  
  const client = this.clients.find(c => c.id == clientId);
  return client ? client.name : `ID ${clientId} no encontrado`;
}

  cleanFilters() {
    this.filterGameId = null;
    this.filterClientId = null;
    this.filterDate = null;
    this.loadPage();
  }

  createLoan() {
    this.dialog.open(LoanEditComponent, { data: {} }).afterClosed().subscribe(res => res && this.loadPage());
  }

  editLoan(loan: Loan) {
    this.dialog.open(LoanEditComponent, { data: { loan } }).afterClosed().subscribe(res => res && this.loadPage());
  }

  deleteLoan(loan: Loan) {
    if (loan.id === undefined) return;
    this.dialog.open(DialogConfirmationComponent, {
      data: { title: 'Eliminar préstamo', description: '¿Desea revocar el préstamo del juego?' }
    }).afterClosed().subscribe(res => {
      if (res) this.loanService.deleteLoan(loan.id!).subscribe(() => this.loadPage());
    });
  }
}
