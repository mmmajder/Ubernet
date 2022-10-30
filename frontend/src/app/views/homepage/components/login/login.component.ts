import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../../../services/auth.service";
import {GoogleLoginProvider, SocialAuthService} from 'angularx-social-login';
import {Router} from '@angular/router';
import {UserService} from "../../../../services/user.service";

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

  constructor(private authService: AuthService, private router: Router, private socialAuthService: SocialAuthService, private userService: UserService) {
  }

  ngOnInit(): void {
  }

  loginWithGoogle(): void {
    this.socialAuthService.signIn(GoogleLoginProvider.PROVIDER_ID)
      .then(() => {
        // let user = this.socialAuthService.authState.pipe(
        //   map((socialUser: SocialUser) => !!socialUser),
        //   tap((isLoggedIn: boolean) => {
        //     if (!isLoggedIn) {
        //       this.router.navigate(['login']);
        //     }
        //   })
        // );
        this.socialAuthService.authState.subscribe(value => {
          console.log(value);
          this.email = value.email;
        })
        let response = this.userService.getUser(this.email);

        console.log(response)
        this.router.navigate(['customer']);
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
      })
  }

  switchToRegisterForm() {
    this.switchForm.emit();
  }

  login() {
    this.authService.login({"email": this.email, "password": this.password});
  }

}
