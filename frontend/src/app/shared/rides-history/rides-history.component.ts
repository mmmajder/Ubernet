import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {RidesHistoryService} from "../../services/rides-history.service";
import {RidesDataSource} from "../../model/Ride";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {merge, tap} from "rxjs";

@Component({
  selector: 'app-rides-history',
  templateUrl: './rides-history.component.html',
  styleUrls: ['./rides-history.component.css']
})
export class RidesHistoryComponent implements OnInit {

  @Input() driverEmail: string;
  @Input() customerEmail: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource: RidesDataSource;
  displayedColumns = ["id", "route", "price", "start", "end", "details"];

  constructor(private ridesHistoryService: RidesHistoryService) {
  }

  ngOnInit() {
    this.dataSource = new RidesDataSource(this.ridesHistoryService);
    this.dataSource.loadRides();
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
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

  detailsAboutRide(id: number) {
    console.log(id);
  }
}
