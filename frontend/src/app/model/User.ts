export class User {
  email!: string;
  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
  role!: string;
  blocked!: boolean;
}

export class Customer {
  constructor(name: string, surname: string, email: string, phoneNumber: string, city: string) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }

  email!: string;
  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
}

export class UserDTO {
  constructor(name: string, surname: string, phoneNumber: string, city: string) {
    this.name = name;
    this.surname = surname;
    this.city = city;
    this.phoneNumber = phoneNumber;
  }

  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
}

export class SimpleUser {
  name!: string;
  surname!: string;
  email!: string;
}
