import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Restaurant, RestaurantService} from "../../../../services/restaurant.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";
import {MatDialogRef} from "@angular/material/dialog";

export interface RestaurantRequestModel {
  name: string;
  address: string;
  phone: string;
  email: string;
  deliveryRate: number;
}

@Component({
  selector: 'app-create-restaurant-dialog',
  templateUrl: './create-restaurant-dialog.component.html',
  styleUrls: ['./create-restaurant-dialog.component.css']
})
export class CreateRestaurantDialogComponent implements OnInit {

  restaurantForm!: FormGroup;

  constructor(private restaurantService: RestaurantService, private _snackBar: MatSnackBar,
              private dialogRef: MatDialogRef<CreateRestaurantDialogComponent>) {
  }

  ngOnInit(): void {
    this.restaurantForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(3)]),
      address: new FormControl('', [Validators.required, Validators.minLength(3)]),
      phone: new FormControl('', [Validators.required, Validators.minLength(3)]),
      email: new FormControl('', [Validators.required, Validators.email]),
      deliveryRate: new FormControl('', [Validators.required, Validators.min(0)])
    });
  }

  createRestaurant() {
    this.dialogRef.close({data: this.restaurantForm?.value});
    this._snackBar.open("Successfully created new restaurant!", "X", {duration: 2000});
  }
}
