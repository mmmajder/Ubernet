import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {Logout} from "../../../store/actions/authentication.actions";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {User} from "../../../model/User";
import {CustomersService} from "../../../services/customers.service";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {

  isActive: boolean = false;
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;

  user: User;
  @Input() currentPage: string = 'dashboard';
  numberOfTokens: number = 0;

  someMethod() {
    this.trigger?.openMenu();
  }

  constructor(private store: Store, private router: Router, private customerService: CustomersService) {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.user = user;
        this.setNumberOfTokens();
      }
    })
  }

  ngOnInit(): void {
  }

  setNumberOfTokens() {
    if (this.user.role == "CUSTOMER") {
      this.customerService.getNumberOfTokens(this.user.email).subscribe({
        next: value => this.numberOfTokens = value
      })
    }
  }

  toggle() {
    this.isActive = !this.isActive;
  }

  logout() {
    this.store.dispatch(new Logout()).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => alert("Error occurred")
    });
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }
}
