import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MatSort, Sort} from "@angular/material/sort";
import {Restaurant, RestaurantService} from "../../../../services/restaurant.service";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {FormControl} from "@angular/forms";
import {Observable} from "rxjs";
import {CreateRestaurantDialogComponent, RestaurantRequestModel} from "../create-restaurant-dialog/create-restaurant-dialog.component";
import {RestaurantsStore} from "../../../../shared/stores/restaurants.store";

@Component({
  selector: 'app-restaurant-table',
  templateUrl: './restaurant-table.component.html',
  styleUrls: ['./restaurant-table.component.css'],
})
export class RestaurantTableComponent implements OnInit {
  @Input() restaurants: Observable<Restaurant[]> | undefined;

  @Output() restaurantEvent = new EventEmitter<RestaurantRequestModel>();

  displayedColumns: string[] = ['name', 'address', 'phone', 'email', 'deliveryRate'];
  dataSource: MatTableDataSource<Restaurant> = new MatTableDataSource<Restaurant>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private restaurantService: RestaurantService, public createRestaurantDialog: MatDialog,
              private cdr: ChangeDetectorRef) {
  }

  ngOnInit() {
    this.restaurants?.subscribe(value => {
      this.dataSource = new MatTableDataSource(Array.from(value));
      this.ngAfterViewInit();
    });
    this.cdr.detectChanges();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  createRestaurant(payload: RestaurantRequestModel) {
    this.restaurantEvent.emit(payload);
  }

  openCreateRestaurantDialog() {
    let dialogRef = this.createRestaurantDialog.open(CreateRestaurantDialogComponent);
    dialogRef.afterClosed().subscribe(res => {
      if (res !== undefined)
        this.createRestaurant(res.data)
    })
  }

  sortData(sort: Sort) {
    if (!sort.active || sort.direction !== '') {
      this.dataSource.data.sort((a: Restaurant, b: Restaurant) => {
        const isAsc = sort.direction === 'asc';
        switch (sort.active) {
          case 'name':
            return compare(a.name, b.name, isAsc);
          case 'address':
            return compare(a.address, b.address, isAsc);
          case 'phone':
            return compare(a.phone, b.phone, isAsc);
          case 'email':
            return compare(a.email, b.email, isAsc);
          case 'deliveryRate':
            return compare(a.deliveryRate, b.deliveryRate, isAsc);
          default:
            return 0;
        }
      });
    }
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
