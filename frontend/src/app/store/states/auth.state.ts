import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {Login, Logout} from "../actions/authentication.actions";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../../model/LoginResponseDto";
import {UserRole} from "../../model/UserRole";

const asyncLocalStorage = {
  setItem: function (key:string, value:string) {
    return Promise.resolve().then(function () {
      localStorage.setItem(key, value);
    });
  },
  getItem: function (key:string) {
    return Promise.resolve().then(function () {
      return localStorage.getItem(key);
    });
  }
};

@State<LoginResponseDto>({
  name: 'auth',
  defaults: {
    token: {
      accessToken: '',
      expiresIn: 0
    },
    userRole: UserRole.CUSTOMER
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
        asyncLocalStorage.setItem('token', "Bearer " + JSON.parse(JSON.stringify(result.token.accessToken))).then(function () {
          return asyncLocalStorage.getItem('token');
        }).then(function () {
          ctx.patchState({
            token: result.token,
            userRole: result.userRole
          });
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
          userRole: UserRole.CUSTOMER
        });
      })
    );
  }

}
