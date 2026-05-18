import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
    selector: 'app-dialog-error',
    standalone: true,
    imports: [MatButtonModule],
    template: `
    <div class="error-dialog-container">
      <h1>{{ data.title }}</h1>
      <div class="alert-error-msg" [innerHTML]="data.description"></div>
      <div class="buttons">
        <button mat-flat-button color="primary" (click)="close()">Aceptar</button>
      </div>
    </div>
  `,
    styles: [`
    .error-dialog-container {
      min-width: 400px;
      max-width: 550px;
      padding: 24px;
      h1 { font-size: 20px; margin-bottom: 16px; color: #1e293b; }
      .buttons { text-align: right; margin-top: 24px; }
    }
    .alert-error-msg {
      color: #dc2626;
      font-size: 13px;
      font-weight: 500;
      background-color: #fef2f2;
      padding: 10px 14px;
      border-radius: 6px;
      border: 1px solid #fecaca;
    }
  `]
})
export class DialogErrorComponent {
    protected readonly dialogRef = inject(MatDialogRef<DialogErrorComponent>);
    protected readonly data = inject(MAT_DIALOG_DATA);

    close() {
        this.dialogRef.close();
    }
}
