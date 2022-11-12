import {LoginCredentials} from '../../model/LoginCredentials';

export class Login {
  static readonly type = '[Auth] Login';
  constructor(public payload: LoginCredentials) {}
}

export class Logout {
  static readonly type = '[Auth] Logout';
}
