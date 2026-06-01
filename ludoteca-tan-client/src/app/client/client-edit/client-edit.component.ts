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
    protected readonly name = signal<string | null>(null);
    protected readonly errorMessage = signal<string>('');

    ngOnInit(): void {
        this.loadFormData(this.data.client ?? null);
    }

    loadFormData(initialData: Client | null): void {
        this.id.set(initialData?.id ?? null);
        this.name.set(initialData?.name ?? null);
    }

    onSave() {
        const id = this.id();
        const name = this.name();

        if (!name) {
            return;
        }

        const client = { id, name } as Client;
        this.clientService.saveClient(client).subscribe(() => {
            this.dialogRef.close(true);
        });
    }

    onClose() {
        this.dialogRef.close(false);
    }
}


