import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {SimpleUser} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class CustomersService {

  private readonly customerUrl: string;

  constructor(private http: HttpClient) {
    this.customerUrl = 'http://localhost:8000/customer';
  }

  public getCustomers(): Observable<SimpleUser[]> {
    return this.http.get<SimpleUser[]>(this.customerUrl + "/get-customers", AuthService.getHttpOptions());
  }

  public getNumberOfTokens(email: string): Observable<number> {
    return this.http.get<number>(this.customerUrl + "/get-number-of-tokens/" + email, AuthService.getHttpOptions());
  }

  getCustomersEmails(): Observable<string[]> {
    return this.http.get<string[]>(this.customerUrl + "/getCustomersEmails", AuthService.getHttpOptions());
  }
}
