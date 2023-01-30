import {Car} from "./Car";

export class User {
  email!: string;
  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
  role!: string;
  blocked!: boolean;
}

export function userIsDriver(user: User): user is Driver {
  return user.role === "DRIVER"
}

export class Driver implements User {
  blocked = false;
  city = "";
  email = "";
  name = "";
  phoneNumber = "";
  role = "";
  surname = "";
  car: Car;
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
  isActive!: boolean;
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
