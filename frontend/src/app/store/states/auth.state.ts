import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {Login, LoginSocial, Logout} from "../actions/authentication.actions";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../../model/LoginResponseDto";
import {UserRole} from "../../model/UserRole";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";

@State<LoginResponseDto>({
  name: 'auth',
  defaults: {
    token: {
      accessToken: '',
      expiresIn: 0
    },
    userRole: UserRole.UNAUTHORIZED
  }
})
@Injectable()
export class AuthState {
  @Selector()
  static token(state: LoginResponseDto): UserTokenState | '' {
    return state.token;
  }

  @Selector()
  static isAuthenticated(state: LoginResponseDto): boolean {
    return !!state.token;
  }

  constructor(private router: Router, private authService: AuthService, private _snackBar: MatSnackBar) {
  }

  @Action(Login)
  login(ctx: StateContext<LoginResponseDto>, action: Login) {
    return this.authService.login(action.payload).pipe(
      tap((result: LoginResponseDto) => {
        console.log("STATE")
        console.log(result.token)
        ctx.patchState({
          token: result.token,
          userRole: result.userRole
        });
      })
    );
  }

  @Action(LoginSocial)
  loginSocial(ctx: StateContext<LoginResponseDto>, action: LoginSocial) {
    return this.authService.loginSocial(action.payload).pipe(
      tap((result: LoginResponseDto) => {

        ctx.patchState({
          token: result.token,
          userRole: result.userRole
        });
      })
    );
  }


  @Action(Logout)
  logout(ctx: StateContext<LoginResponseDto>) {
    const state = ctx.getState();
    return this.authService.logout(state.token).pipe(
      tap({
        next: () => {
          console.log("A")
          ctx.setState({
            token: new UserTokenState(),
            userRole: UserRole.UNAUTHORIZED
          });
          localStorage.clear();
          // this.router.navigate(['/'])
        },
        error: (message) => {
          console.log("B")
          this._snackBar.open(message.error, '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
          this.router.navigate(['/map'])
        }
      })
    );
  }

}
