import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {Logout} from "../../../store/actions/authentication.actions";
import {Select, Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {User} from "../../../model/User";

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

  someMethod() {
    this.trigger?.openMenu();
  }

  constructor(private store: Store, private router: Router) {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => this.user = user
    })
  }

  ngOnInit(): void {
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
