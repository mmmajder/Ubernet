import {Customer} from "./User";

export class CustomerPayment {
  id: number;
  customer: Customer;
  payed: boolean;
  url: string;
  pricePerCustomer: number;
}
