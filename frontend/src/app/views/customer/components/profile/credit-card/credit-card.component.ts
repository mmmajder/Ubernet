import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

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

  constructor() {
  }

  ngOnInit(): void {
  }

}
