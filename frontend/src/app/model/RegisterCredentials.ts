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
