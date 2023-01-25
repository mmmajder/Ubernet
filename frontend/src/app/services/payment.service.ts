import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CreditCard} from "../model/CreditCard";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private readonly creditCardUrl: string;
  // paypalUrl etc.

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.creditCardUrl = 'http://localhost:8000/creditCard';
  }

  public putCreditCardData(email: string, creditCard: CreditCard){
    return this.http.put<Object>(this.creditCardUrl + "/add/" + email, creditCard, AuthService.getHttpOptions());
  }

  public getCreditCard(clientEmail:string){
    return this.http.get(this.creditCardUrl + "/" + clientEmail, this.httpOptions);
  }
}
