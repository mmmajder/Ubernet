import {Driver} from "./User";

export class RegisterCredentials {
  email!: string;
  name!: string;
  surname!: string;
  password!: string;
  phoneNumber!: string;
  city!: string;
}

export class RegisterDriverCredentials {
  email!: string;
  name!: string;
  surname!: string;
  password!: string;
  phoneNumber!: string;
  city!: string;
  allowsPets: boolean;
  allowsBabies: boolean;
  carName: string;
  carType: string;
  plates: string;
}

export class DriverChangeRequest {
  email!: string;
  name!: string;
  surname!: string;
  phoneNumber!: string;
  city!: string;
  allowsPets: boolean;
  allowsBabies: boolean;
  carName: string;
  carType: string;
  plates: string;
  alreadyRequestedChanges: boolean;
}

export class ProfileChangesRequest {
  email!: string;
  name!: string;
  surname!: string;
  phoneNumber!: string;
  city!: string;
  allowsPets: boolean;
  allowsBabies: boolean;
  carName: string;
  carType: string;
  plates: string;
  isProcessed: boolean;
  requestTime: string;
  driver: Driver;
}
