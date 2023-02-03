import {Component, OnInit} from '@angular/core';
import {User} from "../../../model/User";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-profile-container',
  templateUrl: './profile-container.component.html',
  styleUrls: ['./profile-container.component.css']
})
export class ProfileContainerComponent implements OnInit {
  public user: User = new User();

  constructor(private store: Store, private router: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe({
      next: (resp) => {
        this.user.name = resp.name;
        this.user.surname = resp.surname;
        this.user.phoneNumber = resp.phoneNumber;
        this.user.city = resp.city;
        this.user.email = resp.email;
        this.user.role = resp.role;
      }
    });
  }
}
