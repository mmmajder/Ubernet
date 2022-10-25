import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {Observable} from "rxjs";
import {Restaurant} from "../../../../services/restaurant.service";
import {AuthStore} from "../../../../shared/stores/auth.store";
import {GoogleLoginProvider, SocialAuthService} from 'angularx-social-login';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @Output()
  switchForm = new EventEmitter();

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required]);

  hide: boolean = true;

  email: string = "";
  password: string = "";

  // authService: AuthService;

  // constructor(authService: AuthService) {
  //   this.authService = authService;
  // }

  constructor(private authService: AuthService, private router: Router, private socialAuthService: SocialAuthService) {
    // this.authService = authService;
  }

  ngOnInit(): void {
  }

  loginWithGoogle(): void {
    console.log(this.socialAuthService);
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then(() => this.router.navigate(['mainpage']));
  }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    this.authService.login({"email": this.email, "password": this.password});
  }

}
