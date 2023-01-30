import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {Router} from '@angular/router';
import {Login, LoginSocial} from "../../../../store/actions/authentication.actions";
import {Store} from '@ngxs/store';
import {MatSnackBar} from "@angular/material/snack-bar";
import {FacebookLoginProvider, GoogleLoginProvider, SocialAuthService} from "@abacritt/angularx-social-login";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  @Output() switchForm = new EventEmitter();

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required]);

  hide = true;

  email = "";
  password = "";

  constructor(private _snackBar: MatSnackBar, private authService: AuthService, private store: Store, private router: Router, private socialAuthService: SocialAuthService) {
  }

  facebookSignIn() {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then((res) => {
      console.log(res)
      localStorage.setItem('token', "Bearer " + res.authToken);
      this.loginSocial();
    })
  }

  loginWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then((res) => {
      console.log(res)
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
        next: (value) => this.postLogin(value.auth.token.accessToken),
        error: () => this._snackBar.open("Wrong email or password.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      });
    })
  }

  postLogin(accessToken: string) {
    localStorage.setItem('token', "Bearer " + accessToken);
    setTimeout(() => {
        this.authService.getCurrentlyLoggedUser().subscribe({
          next: (user) => {
            if (user.role === "CUSTOMER")
              this.router.navigate(['/dashboard']);
            else if (user.role === "DRIVER")
              this.router.navigate(['/map']);
            else if (user.role === "ADMIN")
              this.router.navigate(['/analytics']);
          }
        });
      },
      3000);
  }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    this.store.dispatch(new Login({
      "email": this.email,
      "password": this.password
    })).subscribe({
      next: (value) => this.postLogin(value.auth.token.accessToken),
      error: () => this._snackBar.open("Wrong email or password.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }

  forgotPassword() {
    if (this.email != "" && !this.emailFormControl.invalid)
      this.authService.forgotPassword(this.email).subscribe({
        next: () => {
          this._snackBar.open("Check you email to set new password!", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        },
        error: () => {
          this._snackBar.open("Please enter valid email!", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        }
      })
    else {
      this._snackBar.open("Please enter valid email!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    }
  }
}
