import {Component, OnInit, ViewChild} from '@angular/core';
import {RidesHistoryService} from "../../../services/rides-history.service";
import {RidesDataSource} from "../../../model/Ride";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ActivatedRoute} from "@angular/router";
import {merge, tap} from "rxjs";
import {CustomersService} from "../../../services/customers.service";
import {DriversService} from "../../../services/drivers.service";

@Component({
  selector: 'app-rides-history',
  templateUrl: './rides-history.component.html',
  styleUrls: ['./rides-history.component.css']
})
export class RidesHistoryComponent implements OnInit {
  public userRole: string = "";

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  driverEmail: string;
  customerEmail: string;

  dataSource: RidesDataSource;
  displayedColumns = ["id", "route", "price", "start", "end", "details"];
  driversEmails: string[];
  customersEmails: string[];
  search: string = "";

  constructor(private ridesHistoryService: RidesHistoryService, private route: ActivatedRoute, private customersService: CustomersService, private driversService: DriversService) {
  }

  ngOnInit() {
    this.dataSource = new RidesDataSource(this.ridesHistoryService);
    this.dataSource.loadRides();

    this.driversService.getDriversEmails().subscribe({
      next: res => this.driversEmails = res
    });
    this.customersService.getCustomersEmails().subscribe({
      next: res => this.customersEmails = res
    });
  }

  ngAfterViewInit() {
    // reset the paginator after sorting
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    // on sort or paginate events, load a new page
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.loadRides())
      )
      .subscribe();
  }

  loadRides() {
    this.dataSource.loadRides(
      this.driverEmail,
      this.customerEmail,
      this.sort.active,
      this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

  detailsAboutRide(id:number) {
    console.log(id);
  }
}
