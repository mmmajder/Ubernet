import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Customer, User, UserDTO} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': localStorage.getItem('token') || "auth",
    })
  };

  constructor(private http: HttpClient) {
    this.userUrl = 'http://localhost:8000/user';
  }

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(this.userUrl + "?email=" + email, this.httpOptions);
  }

  updateCustomerData(customer: Customer) {
    return this.http.put<UserDTO>(this.userUrl + "/profile?email=" + customer.email, {
      "name": customer.name,
      "surname": customer.surname,
      "phoneNumber": customer.phoneNumber,
      "city": customer.city
    }, this.httpOptions);
  }
}
