import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Customer, User, UserDTO} from "../model/User";
import {AuthService} from "./auth.service";
import {CreditCard} from "../model/CreditCard";
import {PasswordChangeInfo} from "../model/PasswordChangeInfo";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string;
  private readonly authUrl: string;

  constructor(private http: HttpClient) {
    this.userUrl = 'http://localhost:8000/user';
    this.authUrl = 'http://localhost:8000/auth';
  }

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(this.userUrl + "?email=" + email, AuthService.getHttpOptions());
  }

  public updateCustomerData(customer: Customer): Observable<UserDTO> {
    let body = new UserDTO(customer.name, customer.surname, customer.phoneNumber, customer.city);
    return this.http.put<UserDTO>(this.userUrl + "/profile?email=" + customer.email, body, AuthService.getHttpOptions());
  }

  public changePassword(email: string, passwordChangeInfo: PasswordChangeInfo){
    return this.http.put<Object>(this.authUrl + "/changePassword/" + email, passwordChangeInfo, AuthService.getHttpOptions());
  }
}
