import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly loginUrl: string;
  private readonly logoutUrl: string;
  private readonly currentlyLoggedUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': localStorage.getItem('token') || 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.loginUrl = 'http://localhost:8000/auth/login';
    this.logoutUrl = 'http://localhost:8000/auth/logout';
    this.currentlyLoggedUrl = 'http://localhost:8000/auth/currently-logged-user';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    let body = {
      "email": user.email,
      "password": user.password
    }
    console.log(body)
    return this.http.post<LoginResponseDto>(this.loginUrl, body, this.httpOptions);
  }

  logout(token: UserTokenState | "") {
    return this.http.post<LoginResponseDto>(this.logoutUrl, token, this.httpOptions);
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    console.log(localStorage.getItem('token'))
    return this.http.get<User>(this.currentlyLoggedUrl, this.httpOptions);
  }
}
