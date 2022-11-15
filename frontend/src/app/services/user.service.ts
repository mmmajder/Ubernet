import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Customer, User, UserDTO} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string;
  private readonly authUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': localStorage.getItem('token') || "auth",
    })
  };

  constructor(private http: HttpClient) {
    this.userUrl = 'http://localhost:8000/user';
    this.authUrl = 'http://localhost:8000/auth';
  }

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(this.userUrl + "?email=" + email, this.httpOptions);
  }

  public updateCustomerData(customer: Customer): Observable<UserDTO> {
    let body = new UserDTO(customer.name, customer.surname, customer.phoneNumber, customer.city);
    console.log("body")
    console.log(body)
    return this.http.put<UserDTO>(this.userUrl + "/profile?email=" + customer.email, body, this.httpOptions);
  }

  public changePassword(email: String, currentPassword: String, newPassword: String, reEnteredNewPassword: String){
    let body = {"currentPassword": currentPassword,
                "newPassword": newPassword,
                "reEnteredNewPassword": reEnteredNewPassword};
    console.log("body");
    console.log(body);

    return this.http.put<Object>(this.authUrl + "/changePassword/" + email, body, this.httpOptions);
  }
}
