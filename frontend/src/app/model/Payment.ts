import {CustomerPayment} from "./CustomerPayment";

export class Payment{
  totalPrice!: number;
  isAcceptedPayment: boolean;
  customers: CustomerPayment[];
}
