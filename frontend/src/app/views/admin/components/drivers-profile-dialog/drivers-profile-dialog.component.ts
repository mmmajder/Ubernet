import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/User";

@Component({
  selector: 'app-drivers-profile-dialog',
  templateUrl: './drivers-profile-dialog.component.html',
  styleUrls: ['./drivers-profile-dialog.component.css']
})
export class DriversProfileDialogComponent implements OnInit {
  @Input() user: User;

  constructor() { }

  ngOnInit(): void {
  }

}
