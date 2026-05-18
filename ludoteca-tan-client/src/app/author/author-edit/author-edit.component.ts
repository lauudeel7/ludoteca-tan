import { Component, OnInit, inject, model, signal, ChangeDetectorRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthorService } from '../author.service';
import { Author } from '../models/author.model';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-author-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './author-edit.component.html',
  styleUrl: './author-edit.component.scss'
})
export class AuthorEditComponent implements OnInit {
  protected readonly authorService = inject(AuthorService);
  protected readonly dialogRef = inject(MatDialogRef<AuthorEditComponent>);
  protected readonly data = inject(MAT_DIALOG_DATA);
  private readonly cdr = inject(ChangeDetectorRef);

  protected readonly id = model<number | null>(null);

  name: string = '';
  nationality: string = '';
  protected readonly errorMessage = signal<string>('');

  ngOnInit(): void {
    if (this.data?.author) {
      const author = this.data.author;
      this.id.set(author.id ?? null);
      this.name = author.name ?? '';
      this.nationality = author.nationality ?? '';
    }
  }

  onSave() {
    if (!this.name || !this.name.trim() || !this.nationality || !this.nationality.trim()) {
      this.errorMessage.set('Error: El nombre y la nacionalidad son campos obligatorios.');
      return;
    }

    this.authorService.saveAuthor({
      id: this.id() ?? null as any,
      name: this.name,
      nationality: this.nationality
    }).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => {
        console.error('Estatus recibido:', err.status);

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
          this.errorMessage.set('Error: Ya existe un autor registrado con ese nombre.');
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


