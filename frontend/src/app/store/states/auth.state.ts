import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {Login, LoginSocial, Logout, Register, Verify} from "../actions/authentication.actions";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../../model/LoginResponseDto";
import {UserRole} from "../../model/UserRole";

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

  constructor(private authService: AuthService) {
  }

  @Action(Login)
  login(ctx: StateContext<LoginResponseDto>, action: Login) {
    return this.authService.login(action.payload).pipe(
      tap((result: LoginResponseDto) => {
        ctx.patchState({
          token: result.token,
          userRole: result.userRole
        });
      })
    );
  }

  @Action(Register)
  register(ctx: StateContext<string>, action: Register) {
    return this.authService.register(action.payload).pipe(
      tap((result: string) => {
      })
    );
  }

  @Action(Verify)
  verify(ctx: StateContext<string>, action: Verify) {
    return this.authService.verify(action.payload).pipe(
      tap((result: string) => {
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
      tap(() => {
        ctx.setState({
          token: new UserTokenState(),
          userRole: UserRole.UNAUTHORIZED
        });
      })
    );
  }

}
