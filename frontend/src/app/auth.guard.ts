import {Injectable} from "@angular/core";
import {CanActivate} from "@angular/router";
import {Store} from "@ngxs/store";
import {AuthState} from "./store/states/auth.state";

@Injectable()
export class AuthGuard implements CanActivate {
  constructor(private store: Store) {}

  canActivate() {
    return this.store.selectSnapshot(AuthState.isAuthenticated);
  }
}
