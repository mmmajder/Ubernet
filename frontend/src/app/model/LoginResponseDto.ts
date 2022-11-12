import {UserRole} from "./UserRole";

export class UserTokenState {
  accessToken!: string;
  expiresIn!: number;
}

export interface LoginResponseDto {
  token: UserTokenState | null;
  userRole: UserRole | null;
}
