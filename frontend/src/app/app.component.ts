import {Component, OnInit} from '@angular/core';
import {Actions, ofActionDispatched} from "@ngxs/store";
import {Router} from "@angular/router";
import {Logout} from "./store/actions/authentication.actions";
import {AuthGuard} from "./model/auth-guard.service";
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AuthGuard, AuthService]
})
export class AppComponent implements OnInit {
  title = 'ubernet';

  constructor(private actions: Actions, private router: Router) {
  }

  ngOnInit() {
    this.actions.pipe(ofActionDispatched(Logout)).subscribe(() => {
      this.router.navigate(['']);
    });
  }
}
