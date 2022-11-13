import {UserRole} from "./UserRole";

export class UserTokenState {
  accessToken: string;
  expiresIn: number;
}

export class LoginResponseDto {
  token: UserTokenState;
  userRole: UserRole;
}
