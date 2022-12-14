import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {GoogleLoginProvider, SocialAuthService, FacebookLoginProvider} from 'angularx-social-login';
import {Router} from '@angular/router';
import {UserService} from "../../../../services/user.service";
import {Login, LoginSocial} from "../../../../store/actions/authentication.actions";
import {Store} from '@ngxs/store';
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  @Output() switchForm = new EventEmitter();

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required]);

  hide: boolean = true;

  email: string = "";
  password: string = "";

  constructor(private _snackBar: MatSnackBar, private authService: AuthService, private store: Store, private router: Router, private socialAuthService: SocialAuthService, private userService: UserService) {
  }

  ngOnInit(): void {
  }

  facebookSignin() {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then((res) => {
      this.loginSocial();
    })
  }

  loginWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then(() => {
        this.loginSocial();
      })
  }

  private loginSocial() {
    this.socialAuthService.authState.subscribe(value => {
      this.email = value.email;
      this.store.dispatch(new LoginSocial({
        "email": value.email,
        "authToken": value.authToken,
        "firstName": value.firstName,
        "id": value.id,
        "idToken": value.idToken,
        "lastName": value.lastName,
        "name": value.name,
        "photoUrl": value.photoUrl,
        "provider": value.provider,
      })).subscribe({
        next: (newValue) => {
          localStorage.setItem('token', "Bearer " + newValue.auth.token.accessToken);
          this.authService.getCurrentlyLoggedUser();
          this.router.navigate(['/dashboard']);
        },
        error: () => this._snackBar.open("Wrong email or password.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      });
    })
  }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    this.store.dispatch(new Login({
      "email": this.email,
      "password": this.password
    })).subscribe({
      next: (value) => {
        localStorage.setItem('token', "Bearer " + value.auth.token.accessToken);
        this.authService.getCurrentlyLoggedUser();
        this.router.navigate(['/dashboard']);
      },
      error: () => this._snackBar.open("Wrong email or password.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }


}
