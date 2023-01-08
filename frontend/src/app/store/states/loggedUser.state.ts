import {Injectable} from "@angular/core";
import {Action, State, StateContext} from "@ngxs/store";
import {CurrentlyLogged, UpdateCustomerData} from "../actions/loggedUser.actions";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs";
import {User, UserDTO} from "../../model/User";
import {UserService} from "../../services/user.service";

@State<User>({
  name: 'loggedUser',
  defaults: {
    name: '',
    surname: '',
    phoneNumber: '',
    role: '',
    email: '',
    city: '',
    blocked: false
  }
})
@Injectable()
export class LoggedUserState {

  constructor(private authService: AuthService, private userService: UserService) {
  }

  @Action(CurrentlyLogged)
  currentlyLogged(ctx: StateContext<CurrentlyLogged>) {
    return this.authService.getCurrentlyLoggedUser().pipe(
      tap((user: User) => {
        ctx.setState({
          name: user.name,
          surname: user.surname,
          phoneNumber: user.phoneNumber,
          city: user.city,
          role: user.role,
          email: user.email,
          blocked: user.blocked
        });
      })
    );
  }

  @Action(UpdateCustomerData)
  updateCustomerData(ctx: StateContext<UserDTO>, action: UpdateCustomerData) {
    return this.userService.updateCustomerData(action.payload).pipe(
      tap((result: UserDTO) => {
        ctx.patchState({
          name: result.name,
          surname: result.surname,
          city: result.city,
          phoneNumber: result.phoneNumber
        });
      })
    );
  }

}
