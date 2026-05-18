import { Component, OnInit, inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
 
@Component({
  selector: 'app-dialog-confirmation',
  standalone: true, 
  templateUrl: './dialog-confirmation.component.html',
  styleUrls: ['./dialog-confirmation.component.scss']
})
export class DialogConfirmationComponent implements OnInit {
 
  title!: string;
  description!: string;
 

  public dialogRef = inject(MatDialogRef<DialogConfirmationComponent>);
  public data = inject(MAT_DIALOG_DATA);
 
  ngOnInit(): void {
    this.title = this.data?.title;
    this.description = this.data?.description;
  }
 
  onYes() {
    this.dialogRef.close(true);
  }
 
  onNo() {
    this.dialogRef.close(false);
  }
}
