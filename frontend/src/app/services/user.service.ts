import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {Customer, User, UserDTO} from "../model/User";
import {AuthService} from "./auth.service";
import {PasswordChangeInfo} from "../model/PasswordChangeInfo";
import {Fullname} from "../model/Fullname";

export class StringResponse {
  message: string
}

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
    return this.http.get<User>(this.userUrl + "/?email=" + email, AuthService.getHttpOptions());
  }

  public updateCustomerData(customer: Customer): Observable<UserDTO> {
    const body = new UserDTO(customer.name, customer.surname, customer.phoneNumber, customer.city);
    return this.http.put<UserDTO>(this.userUrl + "/profile?email=" + customer.email, body, AuthService.getHttpOptions());
  }

  public changePassword(email: string, passwordChangeInfo: PasswordChangeInfo): Observable<StringResponse> {
    return this.http.put<StringResponse>(this.authUrl + "/changePassword/" + email, passwordChangeInfo, AuthService.getHttpOptions());
  }

  public getUserFullName(email: string): Observable<Fullname> {
    return this.http.get<Fullname>(this.userUrl + "/fullname/" + email, AuthService.getHttpOptions());
  }

  public blockUser(email: string) {
    return this.http.post<boolean>(this.userUrl + "/block?email=" + email, AuthService.getHttpOptions());
  }

  public unblockUser(email: string) {
    return this.http.post<boolean>(this.userUrl + "/unblock?email=" + email, AuthService.getHttpOptions());
  }
}
