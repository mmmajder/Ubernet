import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {CreditCard} from "../../../../../model/CreditCard";
import {AuthService} from "../../../../../services/auth.service";
import {PaymentService} from "../../../../../services/payment.service";

@Component({
  selector: 'app-credit-card',
  templateUrl: './credit-card.component.html',
  styleUrls: ['./credit-card.component.css']
})
export class CreditCardComponent implements OnInit {
  creditCardNumber = "";
  expirationDate = "";
  CVV = "";
  creditCardNumberFormControl = new FormControl("", [Validators.minLength(16), Validators.maxLength(16)]);

  onlyNumPattern = /^[0-9]$/;
  onlyNumPatternReplace = /[^0-9]/g;

  mmYyPattern = /^[0-9/]$/;
  mmYyPatternReplace = /[^0-9/]/g;

  loggedUser: any = null;
  existingCreditCard: CreditCard | null = null;

  isButtonDisabled = true;

  constructor(private paymentService: PaymentService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      //TODO should the loggedUser be taken like this? sometimes because of the async it's not working well
      this.loggedUser = data;
      this.paymentService.getCreditCard(data.email).subscribe(creditCardData => {
        this.existingCreditCard = <CreditCard> creditCardData;
        this.setExistingCardData();
      });
    });
  }

  private setExistingCardData(){
    if (this.existingCreditCard != null){
      this.creditCardNumber = this.existingCreditCard.cardNumber;
      this.expirationDate = this.existingCreditCard.expirationDate;
      this.CVV = this.existingCreditCard.cvv;
    }
  }

  public inputValidator(event: any, pattern: RegExp, patternReplace: RegExp) {
    console.log(event.target.value);
    //let inputChar = String.fromCharCode(event.charCode)
    console.log(!pattern.test(event.target.value));
    if (!pattern.test(event.target.value)) {
      event.target.value = event.target.value.replace(patternReplace, "");
      // invalid character, prevent input
    }
    this.disableButtonIfNoChangeIsMade();
  }

  public onlyNumInputValidator(event: any){
    this.inputValidator(event, this.onlyNumPattern, this.onlyNumPatternReplace);
  }

  public mmYyInputValidator(event: any){
    this.inputValidator(event, this.mmYyPattern, this.mmYyPatternReplace);
  }

  private testCreditCardNumPattern(): boolean{
    console.log(/\d{16}/.test(this.creditCardNumber));
    return /\d{16}/.test(this.creditCardNumber);
  }

  private testCvvPattern(): boolean{
    console.log(/\d{3}/.test(this.CVV));
    return /\d{3}/.test(this.CVV);
  }

  private testExpDatePattern(): boolean{

    if (/([0-9]){2}\/([0-9]){2}/.test(this.expirationDate)) // mm/yy 10/36
    {
      const expDateParts = this.expirationDate.split('/');
      console.log(+expDateParts[0] > 0 && +expDateParts[0] <= 12); // mm/yy 10/36
      return +expDateParts[0] > 0 && +expDateParts[0] <= 12 // mm/yy, months can go from 01 to 12
    }
    console.log(false);
    return false;
  }

  private testCardData(): boolean{
    return this.testCreditCardNumPattern() && this.testExpDatePattern() && this.testCvvPattern();
  }

  public sendCardDataToServer() {
    const creditCard: CreditCard = new CreditCard(this.creditCardNumber, this.expirationDate, this.CVV);
    if (this.testCardData())
      console.log(creditCard);
      this.paymentService.putCreditCardData(this.loggedUser.email, creditCard)
        .subscribe((data) => {
          this.existingCreditCard = <CreditCard> data;
          this.setExistingCardData();
          this.isButtonDisabled = true;
          // TODO notify user about change

        });
      console.log("poslato na server");
  }

  public disableButtonIfNoChangeIsMade(){
    let isCardDataSameAsExistingCard;
    this.isButtonDisabled = false;
    const isCardDataFilledCorrectly = this.testCardData();

    if (this.existingCreditCard === null)
      this.isButtonDisabled = !isCardDataFilledCorrectly;
    else
      isCardDataSameAsExistingCard =  this.creditCardNumber === this.existingCreditCard.cardNumber &&
                                      this.expirationDate === this.existingCreditCard.expirationDate &&
                                      this.CVV === this.existingCreditCard.cvv;
      this.isButtonDisabled = isCardDataSameAsExistingCard || !isCardDataFilledCorrectly;
  }
}
