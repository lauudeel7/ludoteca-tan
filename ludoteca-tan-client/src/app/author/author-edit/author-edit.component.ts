import { Component, OnInit, inject, model, signal, ChangeDetectorRef } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthorService } from '../author.service';
import { Author } from '../models/author.model';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { validateFields } from '../../core/helpers/validation.helper';

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
  errorMessage = signal<string>('');

  protected readonly id = model<number | null>(null);
  protected readonly name = signal<string | null>(null);
  protected readonly nationality = signal<string | null>(null);

  loadFormData(initialData: Author | null) {
    this.id.set(initialData?.id ?? null);
    this.name.set(initialData?.name ?? null);
    this.nationality.set(initialData?.nationality ?? null);
  }

  ngOnInit(): void {
    this.loadFormData(this.data.author ?? null);
  }

  onSave() {
        const id = this.id();
        const name = this.name();
        const nationality = this.nationality();

        const requiredFields = ["name", "nationality"] as const
        const data = { name, nationality }

        if (!validateFields(data, requiredFields)) {
            return;
        }

        const author = {
            id,
            name,
            nationality,
        } as Author;
        this.authorService.saveAuthor(author).subscribe(() => {
            this.dialogRef.close(true);
        });
    }

  onClose() {
    this.dialogRef.close(false);
  }
}


