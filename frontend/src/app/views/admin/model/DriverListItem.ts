export class DriverListItem {
  email!: string;
  name!: string;
  requestedChanges: boolean = false;

  constructor(email: string, name: string, changes: boolean) {
    this.email = email;
    this.name = name;
    this.requestedChanges = changes;
  }
}
