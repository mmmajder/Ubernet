import {Component, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {CurrentlyLogged} from "../../../store/actions/loggedUser.actions";
import {RidesHistoryComponent} from "../../../shared/rides-history/rides-history.component";
import {DriversService} from "../../../services/drivers.service";
import {CustomersService} from "../../../services/customers.service";

@Component({
  selector: 'app-rides-history-container',
  templateUrl: './rides-history-container.component.html',
  styleUrls: ['./rides-history-container.component.css']
})
export class RidesHistoryContainerComponent implements OnInit {
  @ViewChild('child') ridesHistoryComponent: RidesHistoryComponent;

  public userRole: string = "";
  driverEmail: string;
  customerEmail: string;
  driversEmails: string[];
  customersEmails: string[];

  constructor(private store: Store, private router: Router, private driversService: DriversService, private customersService: CustomersService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.userRole = resp.loggedUser.role;
        if (this.userRole == "ADMIN") {
          this.loadCustomerEmails();
          this.loadDriversEmails();
        }
        else if (this.userRole == "DRIVER") {
          this.loadCustomerEmails();
          this.driverEmail = resp.loggedUser.email;
        }
        else if (this.userRole == "CUSTOMER") {
          this.customerEmail = resp.loggedUser.email;
        }
      },
      error: () => this.router.navigate(['/'])
    });
  }

  loadRides() {
    this.ridesHistoryComponent.loadRides();
  }

  loadCustomerEmails() {
    this.customersService.getCustomersEmails().subscribe({
      next: res => this.customersEmails = res
    });
  }

  loadDriversEmails() {
    this.driversService.getDriversEmails().subscribe({
      next: res => this.driversEmails = res
    });
  }
}
