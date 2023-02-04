import {LoginCredentials} from '../../model/LoginCredentials';
import {LoginSocialCredentials} from "../../model/LoginSocialCredentials";

export class Login {
  static readonly type = '[Auth] Login';
  constructor(public payload: LoginCredentials) {}
}

export class LoginSocial {
  static readonly type = '[Auth] Login Social';
  constructor(public payload: LoginSocialCredentials) {}
}

export class Logout {
  static readonly type = '[Auth] Logout';
}
