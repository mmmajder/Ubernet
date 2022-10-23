import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {Observable} from "rxjs";
import {Restaurant} from "../../../../services/restaurant.service";
import {AuthStore} from "../../../../shared/stores/auth.store";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  hide: boolean = true;

  email: string = "";
  password: string = "";


  authService: AuthService;

  constructor(authService: AuthService) {
    this.authService = authService;
  }

  ngOnInit(): void {
  }

  login() {
    this.authService.login({"email": this.email, "password": this.password});
  }

}
