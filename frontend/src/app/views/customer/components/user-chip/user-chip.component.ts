import {Component, Input} from '@angular/core';
import {Router} from "@angular/router";
import {ActivatedRoute} from '@angular/router';
import {Store} from "@ngxs/store";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthService} from "../../../../services/auth.service";
import {SimpleUser} from "../../../../model/User";
import {
  CustomersProfileDialogComponent
} from "../../../admin/components/customers-profile-dialog/customers-profile-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-user-chip',
  templateUrl: './user-chip.component.html',
  styleUrls: ['./user-chip.component.css']
})
export class UserChipComponent {

  @Input() customers: SimpleUser[]
  photo: string;

  constructor(private customersProfile: MatDialog, private authService: AuthService, private _snackBar: MatSnackBar) {
    this.photo = '../../assets/default-profile-picture.jpg';
  }

  openUserDialog(customer: SimpleUser) {
    const dialogRef = this.customersProfile.open(CustomersProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = customer.email;
  }
}
