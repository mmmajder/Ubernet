import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {GoogleLoginProvider, SocialAuthService} from 'angularx-social-login';
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

  loginWithGoogle(): void {
    // window.open("/@{/oauth2/authorization/google", "_blank")


    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then(() => {
        this.socialAuthService.authState.subscribe(value => {
          console.log("VREDNOST")
          console.log(value);
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
              console.log("AAAAAAAAAAAAAAA")
              console.log(newValue.auth.token);
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
      })
  }

  // let response = this.userService.getUser(this.email);
  //
  // console.log(response)
  // this.router.navigate(['customer']);
  //TODO will be added soon
  // if (response==null) {
  //   // createUser();
  //   this.router.navigate(['customer']);
  // }
  // else if (response.role==UserTypeEnum.CUSTOMER) {
  //   this.router.navigate(['customer']);
  // }
  // else if (response.role==UserTypeEnum.ADMIN) {
  //   this.router.navigate(['admin']);
  // }
  // else if (response.role==UserTypeEnum.DRIVER) {
  //   this.router.navigate(['driver']);
  // }
  // })
  // }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    this.store.dispatch(new Login({
      "email": this.email,
      "password": this.password
    })).subscribe({
      next: (value) => {
        console.log(value.auth.token);
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
