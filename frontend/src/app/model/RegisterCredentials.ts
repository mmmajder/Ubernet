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
  email = "";
  name = "";
  surname = "";
  phoneNumber = "";
  city = "";
  allowsPets = false;
  allowsBabies = false;
  carName = "";
  carType = "";
  plates = "";
  isProcessed = false;
  requestTime = "";
  driver: Driver = new Driver();
}
