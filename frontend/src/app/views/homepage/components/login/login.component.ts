import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
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

  formGroup = this._formBuilder.group({
    emailFormControl: ['email', [Validators.required, Validators.email]],
    passwordFormControl: ['password', [Validators.required]]
  })

  hide = true;

  email = "";
  password = "";

  constructor(private _snackBar: MatSnackBar, private _formBuilder: FormBuilder, private authService: AuthService, private store: Store, private router: Router, private socialAuthService: SocialAuthService) {
  }

  // ngOnInit(): void {
  //   this.loginSocial()
  // }

  singInWithFB() {
    this.socialAuthService.signIn(FacebookLoginProvider.PROVIDER_ID).then((res) => {
      localStorage.setItem('token', "Bearer " + res.authToken);
      this.store.dispatch(new LoginSocial({
        "email": res.email,
        "authToken": res.authToken,
        "firstName": res.firstName,
        "id": res.id,
        "idToken": res.idToken,
        "lastName": res.lastName,
        "name": res.name,
        "photoUrl": res.photoUrl,
        "provider": res.provider,
      })).subscribe({
        next: (value) => this.postLogin(value.auth.token.accessToken),
        error: () => this.openSnack("Wrong email or password.")
      });
    })
  }

  // loginWithGoogle(): void {
  //   this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID).then(() => {
  //     this.loginSocial();
  //   })
  // }

  // private loginSocial() {
  //   this.socialAuthService.authState.subscribe(value => {
  //     if (value !== undefined && value !== null && localStorage.getItem("token") === null)
  //       this.store.dispatch(new LoginSocial({
  //         "email": value.email,
  //         "authToken": value.authToken,
  //         "firstName": value.firstName,
  //         "id": value.id,
  //         "idToken": value.idToken,
  //         "lastName": value.lastName,
  //         "name": value.name,
  //         "photoUrl": value.photoUrl,
  //         "provider": value.provider,
  //       })).subscribe({
  //         next: (value) => this.postLogin(value.auth.token.accessToken),
  //         error: () => this.openSnack("Wrong email or password.")
  //       });
  //   })
  // }

  postLogin(accessToken: string) {
    localStorage.setItem('token', "Bearer " + accessToken);
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
      error: () => this.openSnack("Wrong email or password.")
    });
  }

  forgotPassword() {
    if (this.email != "" && !this.formGroup.controls["emailFormControl"].hasError("email")) {
      this.authService.forgotPassword(this.email).subscribe({
        next: () => this.openSnack("Check you email to set new password!"),
        error: () => this.openSnack("Please enter valid email!")
      })
    } else {
      this.openSnack("Please enter valid email!")
    }
  }

  openSnack(message: string) {
    this._snackBar.open(message, '', {
      duration: 3000,
      panelClass: ['snack-bar']
    })
  }

}
