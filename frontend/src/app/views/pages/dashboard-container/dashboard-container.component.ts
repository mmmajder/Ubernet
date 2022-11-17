import {Component, OnInit} from '@angular/core';
import {CurrentlyLogged} from "../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {User} from "../../../model/User";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user.service";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-dashboard-container',
  templateUrl: './dashboard-container.component.html',
  styleUrls: ['./dashboard-container.component.css']
})
export class DashboardContainerComponent implements OnInit {
  public user: User = new User();

  constructor(private store: Store, private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.user.name = resp.loggedUser.name;
        this.user.surname = resp.loggedUser.surname;
        this.user.phoneNumber = resp.loggedUser.phoneNumber;
        this.user.city = resp.loggedUser.city;
        this.user.email = resp.loggedUser.email;
        this.user.role = resp.loggedUser.role;
      },
      error: () => this.router.navigate(['/'])
    });
  }

}
