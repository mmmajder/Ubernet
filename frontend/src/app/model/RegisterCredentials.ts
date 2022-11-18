import {UserRole} from "./UserRole";

export class RegisterCredentials {
  email!: string;
  name!: string;
  lastName!: string;
  password!: string;
  phoneNumber!: string;
  city!: string;
  userRole: UserRole
}
