import {Injectable} from "@angular/core";
import {ComponentStore, tapResponse} from "@ngrx/component-store";
import {exhaustMap, Observable, tap} from "rxjs";
import {AuthService} from "../../services/auth.service";
import {SocketService} from "../../services/sockets.service";

class User {
  email!: string;
  password!: string;
}

class UserTokenState {
  accessToken!: string;
  expiredIn!: number;
}

enum UserRole {
  ADMIN,
  DRIVER,
  CUSTOMER
}

class LoginResponseDTO {
  userRole!: UserRole;
  token!: UserTokenState;
}

export interface AuthState {
  login: LoginResponseDTO;
}

const INITIAL_STATE: AuthState = {
  login: new LoginResponseDTO()
}

@Injectable()
export class AuthStore extends ComponentStore<AuthState> {

  constructor(private authService: AuthService, private socketService: SocketService) {
    super(INITIAL_STATE);
  }

  // readonly login$: Observable<LoginCredentials> = this.select(state => state.login);

  /*login = this.effect((user$: Observable<LoginCredentials>) => user$
    .pipe(
      exhaustMap(user => {
        return this.authService.login(user)
          .pipe(tapResponse(
            (response) => console.log(response),
            (error) => console.error(error),
          ))
      })
    )
  )*/

}
