import { Component, OnInit, inject, model, signal, ChangeDetectorRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../models/client.model';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-client-edit',
    standalone: true,
    imports: [CommonModule, FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
    templateUrl: './client-edit.component.html',
    styleUrl: './client-edit.component.scss'
})
export class ClientEditComponent implements OnInit {
    protected readonly dialogRef = inject(MatDialogRef<ClientEditComponent>);
    protected readonly data = inject(MAT_DIALOG_DATA);
    protected readonly clientService = inject(ClientService);
    private readonly cdr = inject(ChangeDetectorRef);

    protected readonly id = model<number | null>(null);
    name: string = '';
    protected readonly errorMessage = signal<string>('');

    ngOnInit(): void {
        if (this.data?.client) {
            const client = this.data.client;
            this.id.set(client.id ?? null);
            this.name = client.name ?? '';
        }
    }

    onSave() {
        if (!this.name || !this.name.trim()) {
            this.errorMessage.set('Error: El nombre del cliente es un campo obligatorio.');
            return;
        }

        this.clientService.saveClient({ id: this.id() || undefined, name: this.name.trim() }).subscribe({
            next: () => this.dialogRef.close(true),
            error: (err) => {
                let msg = '';
                if (err.error) {
                    if (typeof err.error === 'string') msg = err.error;
                    else if (typeof err.error === 'object' && err.error.message) {
                        msg = Array.isArray(err.error.message) ? err.error.message.join(' ') : String(err.error.message);
                    }
                }
                const upperMsg = msg.toUpperCase();

                if (
                    err.status === 400 ||
                    err.status === 409 ||
                    err.status === 500 ||
                    upperMsg.includes('EXISTS') ||
                    upperMsg.includes('DUPLICAT') ||
                    upperMsg.includes('CONSTRAINT')
                ) {
                    this.errorMessage.set('Error: Ya existe un cliente registrado con ese nombre.');
                } else {
                    this.errorMessage.set('Error inesperado al guardar.');
                }
                this.cdr.detectChanges();
            }
        });
    }

    onClose() {
        this.dialogRef.close(false);
    }
}


