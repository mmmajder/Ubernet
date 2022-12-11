import {Component, ElementRef, OnInit} from '@angular/core';
import {RidesHistoryService} from "../../../services/rides-history.service";
import {Ride, RidesDataSource} from "../../../model/Ride";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {ActivatedRoute} from "@angular/router";
import {debounceTime, distinctUntilChanged, fromEvent, merge, tap} from "rxjs";
import {CustomersService} from "../../../services/customers.service";
import {DriversService} from "../../../services/drivers.service";

@Component({
  selector: 'app-rides-history',
  templateUrl: './rides-history.component.html',
  styleUrls: ['./rides-history.component.css']
})
export class RidesHistoryComponent implements OnInit {
  public userRole: string = "";
  rides: Ride[];

  private paginator: MatPaginator;
  private sort: MatSort;
  input: ElementRef;
  driverEmail: string;
  customerEmail: string;

  dataSource: RidesDataSource;
  displayedColumns = ["seqNo", "description", "duration"];
  driversEmails: string[];
  customersEmails: string[];

  constructor(private ridesHistoryService: RidesHistoryService, private route: ActivatedRoute, private customersService: CustomersService, private driversService: DriversService) {
  }

  ngOnInit() {
    this.rides = this.route.snapshot.data["rides"];
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

    // server-side search
    fromEvent(this.input.nativeElement, 'keyup')
      .pipe(
        debounceTime(150),
        distinctUntilChanged(),
        tap(() => {
          this.paginator.pageIndex = 0;
          this.loadRides();
        })
      )
      .subscribe();

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
      this.input.nativeElement.value,
      this.driverEmail,
      this.customerEmail,
      this.sort.direction,
      this.paginator.pageIndex,
      this.paginator.pageSize);
  }

}
