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

  private readonly authUrl: string;

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8000/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    let body = {
      "email": user.email,
      "password": user.password
    }
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', body, AuthService.getHttpOptions());
  }

  public logout(token: UserTokenState | ""): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/logout', token, AuthService.getHttpOptions());
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/currently-logged-user', AuthService.getHttpOptions());
  }

  public static getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }
}
