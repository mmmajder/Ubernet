import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private readonly creditCardUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.creditCardUrl = 'http://localhost:8000/creditCard';
  }

  public getCreditCard(clientEmail: string) {
    return this.http.get(this.creditCardUrl + "/" + clientEmail, this.httpOptions);
  }
}
