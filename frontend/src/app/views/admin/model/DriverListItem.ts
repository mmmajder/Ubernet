export class DriverListItem {
  email!: string;
  name!: string;
  isWorking!: boolean;

  constructor(email: string, name: string, isWorking: boolean) {
    this.email = email;
    this.name = name;
    this.isWorking = isWorking;
  }
}
