import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../models/client.model';
import { ClientEditComponent } from '../client-edit/client-edit.component';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { DialogErrorComponent } from '../../core/dialog-error/dialog-error-component';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    CommonModule,
  ],
  templateUrl: './client-list.page.html',
  styleUrl: './client-list.page.scss',
})
export class ClientListPage implements OnInit {
  dataSource = new MatTableDataSource<Client>();
  displayedColumns: string[] = ['id', 'name', 'action'];

  protected readonly clientService = inject(ClientService);
  protected readonly dialog = inject(MatDialog);

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.clientService
      .getClients()
      .subscribe((clients) => (this.dataSource.data = clients));
  }

  createClient() {
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!result) return;
      this.loadData();
    });
  }

  editClient(client: Client) {
    const dialogRef = this.dialog.open(ClientEditComponent, {
      data: { client },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (!result) return;
      this.loadData();
    });
  }

  deleteClient(client: Client) {
    if (client.id === undefined) return;

    this.clientService.checkClientLoans(client.id).subscribe((hasLoans) => {
      // Si tiene préstamos, muestra error y detenemos la ejecución aquí
      if (hasLoans) {
        this.dialog.open(DialogErrorComponent, {
          data: {
            title: 'Operación denegada',
            description: 'No se puede eliminar al cliente porque tiene préstamos asociados activos.<br>Revoca primero sus préstamos.'
          }
        });
      } else {
        // Si no tiene préstamos, procedemos con la confirmación de borrado
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
          data: {
            title: 'Eliminar cliente',
            description: 'Atención si borra el cliente se perderán sus datos.<br> ¿Desea eliminar el cliente?'
          }
        });

        dialogRef.afterClosed().subscribe(result => {
          if (result) {
            this.clientService.deleteClient(client.id!).subscribe({
              next: () => {
                this.loadData();
              },
              // 2. Capturamos cualquier BadRequestException inesperada del backend
              error: (err) => {
                const message = err.error?.message || 'No se pudo eliminar el cliente.';
                this.dialog.open(DialogErrorComponent, {
                  data: {
                    title: 'Error al eliminar',
                    description: message
                  }
                });
              }
            });
          }
        });
      }
    });
  }
}
