import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-drivers-profile-dialog',
  templateUrl: './drivers-profile-dialog.component.html',
  styleUrls: ['./drivers-profile-dialog.component.css']
})
export class DriversProfileDialogComponent implements OnInit {
  @Input() userEmail: string;

  constructor() {
  }

  ngOnInit(): void {
  }

}
