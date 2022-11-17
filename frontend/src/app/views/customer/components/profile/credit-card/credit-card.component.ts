import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {CreditCard} from "../../../../../model/CreditCard";
import {UserService} from "../../../../../services/user.service";
import {AuthService} from "../../../../../services/auth.service";

@Component({
  selector: 'app-credit-card',
  templateUrl: './credit-card.component.html',
  styleUrls: ['./credit-card.component.css']
})
export class CreditCardComponent implements OnInit {
  creditCardNumber: string = "";
  expirationDate: string = "";
  CVV: string = "";
  creditCardNumberFormControl = new FormControl("", [Validators.minLength(16), Validators.maxLength(16)]);

  onlyNumPattern: RegExp = /^[0-9]$/;
  onlyNumPatternReplace: RegExp = /[^0-9]/g;

  mmYyPattern: RegExp = /^[0-9\/]$/;
  mmYyPatternReplace: RegExp = /[^0-9\/]/g;

  loggedUser: any = null;

  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      //TODO should the loggedUser be taken like this? sometimes because of the async it's not working well
      this.loggedUser = data;
    });
  }

  public inputValidator(event: any, pattern: RegExp, patternReplace: RegExp) {
    console.log(event.target.value);
    //let inputChar = String.fromCharCode(event.charCode)
    console.log(!pattern.test(event.target.value));
    if (!pattern.test(event.target.value)) {
      event.target.value = event.target.value.replace(patternReplace, "");
      // invalid character, prevent input

    }
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
      let expDateParts = this.expirationDate.split('/');
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
    let creditCard: CreditCard = new CreditCard(this.creditCardNumber, this.expirationDate, this.CVV);
    if (this.testCardData())
      console.log(creditCard);
      this.userService.putCreditCardData(this.loggedUser.email, creditCard)
        .subscribe((data) => {
          console.log(data);
        });
      console.log("poslato na server");
  }
}
