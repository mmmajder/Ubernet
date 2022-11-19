export class CreditCard {
  cardNumber!: string;
  expirationDate!: string;
  cvv!: string;

  constructor(creditCardNumber: string, expirationDate: string, CVV: string) {
    this.cardNumber = creditCardNumber;
    this.expirationDate = expirationDate;
    this.cvv = CVV;
  }
}
