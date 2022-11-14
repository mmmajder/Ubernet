import {Customer} from "../../model/User";

export class CurrentlyLogged {
  static readonly type = '[Auth] Currently Logged';
}

export class UpdateCustomerData {
  static readonly type = '[Auth] Update Customer Data';

  constructor(public payload: Customer) {}
}
