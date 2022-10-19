import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CreateMenuItemDialogComponent} from "../create-menu-item-dialog/create-menu-item-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MenuItem, Restaurant} from "../../../../services/restaurant.service";
import {Sort} from "@angular/material/sort";
import {Observable} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface Data {
  kind: string;
  value: string;
}

export interface RemoveMenuItemRequest {
  restaurantId: string,
  name: string
}

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit {
  @Input() restaurant: Observable<Restaurant> = new Observable<Restaurant>();

  @Output() addMenuItemEvent = new EventEmitter<MenuItem>();
  @Output() removeMenuItemEvent = new EventEmitter<RemoveMenuItemRequest>();

  dataSource: Data[] = [];
  displayedColumns: string[] = ['kind', 'value'];
  menuItems: MenuItem[] = [];
  displayedColumnsMenu: string[] = ['name', 'description', 'price'];
  restaurantName: string = "Restaurant name";
  selectedRow: string = '';
  id: string = '';

  constructor(public dialog: MatDialog, private _snackBar: MatSnackBar, private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.restaurant.subscribe(value => {
      this.setRestaurantData(value);
    });
    this.cdr.detectChanges();
  }

  setRestaurantData(restaurant: Restaurant): void {
    this.restaurantName = restaurant.name;
    this.dataSource = [
      {kind: "Address", value: restaurant.address},
      {kind: "Email", value: restaurant.email},
      {kind: "Phone", value: restaurant.phone},
      {kind: "Delivery Rate", value: restaurant.deliveryRate.toString()},
    ];
    this.menuItems = restaurant.menuItems;
    this.id = restaurant.id;
  }

  openDialog() {
    let dialogRef = this.dialog.open(CreateMenuItemDialogComponent);
    dialogRef.afterClosed().subscribe(res => {
      if (res !== undefined)
        this.createMenuItem(res.data)
    })
  }

  createMenuItem(payload: MenuItem) {
    payload.id = this.id;
    if (this.uniqueName(payload.name)) {
      this.addMenuItemEvent.emit(payload);
      this._snackBar.open("Successfully created new menu item!", "X", {duration: 2000});
    } else
      this._snackBar.open("All menu item name's must be unique!", "X", {duration: 2000});
  }

  sortData(sort: Sort) {
    const data = this.menuItems.slice();
    if (!sort.active || sort.direction === '') {
      this.menuItems = data;
      return;
    }
    this.menuItems = data.sort((a: MenuItem, b: MenuItem) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name':
          return compare(a.name, b.name, isAsc);
        case 'description':
          return compare(a.description, b.description, isAsc);
        case 'price':
          return compare(a.price, b.price, isAsc);
        default:
          return 0;
      }
    });
  }

  selectRow(row: { name: string; }) {
    this.selectedRow = row.name;
  }

  removeMenuItem() {
    if (this.selectedRow !== '') {
      if (this.id != null)
        this.removeMenuItemEvent.emit({"restaurantId": this.id, "name": this.selectedRow});
    } else {
      this._snackBar.open("You have to select a menu item first!", "X", {duration: 2000});
    }
  }

  private uniqueName(name: string) {
    for (let i = 0; i < this.menuItems.length; i++) {
      if (this.menuItems[i].name === name)
        return false;
    }
    return true;
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
