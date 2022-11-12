import {Injectable} from "@angular/core";
import {Action, State, StateContext} from "@ngxs/store";
import {CurrentlyLogged} from "../actions/loggedUser.actions";
import {AuthService} from "../../services/auth.service";
import {tap} from "rxjs";
import {User} from "../../model/User";

@State<User>({
  name: 'loggedUser',
  defaults: {
    name: '',
    surname: '',
    phoneNumber: '',
    role: '',
    email: '',
    city: ''
  }
})
@Injectable()
export class LoggedUserState {

  constructor(private authService: AuthService) {
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
          email: user.email
        });
      })
    );
  }

}
