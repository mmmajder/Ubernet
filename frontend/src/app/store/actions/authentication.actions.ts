import {LoginCredentials} from '../../model/LoginCredentials';
import {LoginSocialCredentials} from "../../model/LoginSocialCredentials";
import {RegisterCredentials} from "../../model/RegisterCredentials";
import {VerifyCredentials} from "../../model/VerifyCredentials";

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

export class Register {
  static readonly type = '[Auth] Register';
  constructor(public payload: RegisterCredentials) {}
}

export class Verify {
  static readonly type = '[Auth] Verify';
  constructor(public payload: VerifyCredentials) {}
}
