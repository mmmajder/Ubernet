import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DriverListItem} from "../../model/DriverListItem";

const ELEMENT_DATA: DriverListItem[] = [
  {profilePicture: "assets/taxi.jpg", name: "Pera", lastName: "Peric", activity: true},
  {profilePicture: "assets/taxi.jpg", name: "Pera", lastName: "Peric", activity: false},
  {profilePicture: "assets/taxi.jpg", name: "Pera", lastName: "Peric", activity: true},
  {profilePicture: "assets/taxi.jpg", name: "Pera", lastName: "Peric", activity: false},
  {profilePicture: "assets/taxi.jpg", name: "Pera", lastName: "Peric", activity: true},
];

@Component({
  selector: 'app-drivers-list',
  templateUrl: './drivers-list.component.html',
  styleUrls: ['./drivers-list.component.css']
})
export class DriversListComponent {
  displayedColumns: string[] = ['profilePicture', 'name', 'lastName', 'activity'];
  driversList = new MatTableDataSource(ELEMENT_DATA);

  constructor() {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.driversList.filter = filterValue.trim().toLowerCase();
  }
}
