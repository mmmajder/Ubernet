import {Component, OnInit} from '@angular/core';
import {MenuItem, RestaurantService} from "../../../../services/restaurant.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-create-menu-item-dialog',
  templateUrl: './create-menu-item-dialog.component.html',
  styleUrls: ['./create-menu-item-dialog.component.css']
})
export class CreateMenuItemDialogComponent implements OnInit {
  menuForm!: FormGroup;

  constructor(private restaurantService: RestaurantService,
              private dialogRef: MatDialogRef<CreateMenuItemDialogComponent>) {
  }

  ngOnInit(): void {
    this.menuForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(3)]),
      description: new FormControl('', [Validators.required, Validators.minLength(3)]),
      price: new FormControl('', [Validators.required, Validators.min(0)]),
    });
  }

  createMenuItem() {
    let menuItem = new MenuItem();
    menuItem.id = '';
    menuItem.name = this.menuForm.value.name;
    menuItem.description = this.menuForm.value.description;
    menuItem.price = this.menuForm.value.price;
    this.dialogRef.close({data: menuItem});
  }
}
