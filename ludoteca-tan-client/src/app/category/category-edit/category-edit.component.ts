import { Component, OnInit, inject, model, signal, ChangeDetectorRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../category.service';
import { Category } from '../models/category.model';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './category-edit.component.html',
  styleUrl: './category-edit.component.scss'
})
export class CategoryEditComponent implements OnInit {
  protected readonly dialogRef = inject(MatDialogRef<CategoryEditComponent>);
  protected readonly data = inject(MAT_DIALOG_DATA);
  protected readonly categoryService = inject(CategoryService);
  private readonly cdr = inject(ChangeDetectorRef);

  protected readonly id = model<number | null>(null);
  name: string = '';
  protected readonly errorMessage = signal<string>('');

  ngOnInit(): void {
    if (this.data?.category) {
      const category = this.data.category;
      this.id.set(category.id ?? null);
      this.name = category.name ?? '';
    }
  }

  onSave() {
    if (!this.name || !this.name.trim()) {
      this.errorMessage.set('Error: El nombre de la categoría es obligatorio.');
      return;
    }

    this.categoryService.saveCategory({ id: this.id() || undefined, name: this.name.trim() }).subscribe({
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
          this.errorMessage.set('Error: Ya existe una categoría registrada con ese nombre.');
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
