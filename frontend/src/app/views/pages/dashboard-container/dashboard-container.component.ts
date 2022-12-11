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
  public userRole: string = "";

  constructor(private store: Store, private router: Router) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.userRole = resp.loggedUser.role;
      },
      error: () => this.router.navigate(['/'])
    });
  }

}
