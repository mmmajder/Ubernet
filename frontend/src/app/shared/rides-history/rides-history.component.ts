import {AfterViewInit, Component, Input, ViewChild} from '@angular/core';
import {RidesHistoryService} from "../../services/rides-history.service";
import {RidesDataSource} from "../../model/Ride";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {merge, tap} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {RideDetailsDialogComponent} from "../ride-details-dialog/ride-details-dialog.component";

@Component({
  selector: 'app-rides-history',
  templateUrl: './rides-history.component.html',
  styleUrls: ['./rides-history.component.css']
})
export class RidesHistoryComponent implements AfterViewInit {

  @Input() driverEmail: string;
  @Input() customerEmail: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource: RidesDataSource;
  displayedColumns = ["id", "route", "price", "start", "end", "details"];

  constructor(private ridesHistoryService: RidesHistoryService, public dialog: MatDialog) {
    this.dataSource = new RidesDataSource(this.ridesHistoryService);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadRides())
      )
      .subscribe();
  }

  loadRides(driverEmail: string = this.driverEmail, customerEmail: string = this.customerEmail) {
    this.driverEmail = driverEmail;
    this.customerEmail = customerEmail;
    this.dataSource.loadRides(
      this.driverEmail,
      this.customerEmail,
      this.sort.active,
      this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  detailsAboutRide(id: number) {
    const dialogRef = this.dialog.open(RideDetailsDialogComponent, {
      height: '600px',
      width: '1000px'
    });
    dialogRef.componentInstance.id = id;
    dialogRef.componentInstance.dialogRef = dialogRef;
  }
}
