export class DriverDTO {
  email!: string;
  name!: string;
  surname!: string;
  city!: string;
  phoneNumber!: string;
  role!: string;
  isWorking: boolean;
  requestedChanges: boolean;

  constructor(name: string, surname: string, email: string, phoneNumber: string, city: string, isWorking: boolean) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.city = city;
    this.phoneNumber = phoneNumber;
    this.isWorking = isWorking;
  }
}
