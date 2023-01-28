import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";
import {LoginSocialCredentials} from "../model/LoginSocialCredentials";
import {RegisterCredentials, RegisterDriverCredentials} from "../model/RegisterCredentials";
import {VerifyCredentials} from "../model/VerifyCredentials";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8000/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', user, AuthService.getHttpOptions());
  }

  public register(user: RegisterCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register', user, AuthService.getHttpOptions());
  }

  public registerDriver(user: RegisterDriverCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/registerDriver', user, AuthService.getHttpOptions());
  }

  public verify(credentials: VerifyCredentials): Observable<string> {
    return this.http.get<string>(this.authUrl + `/verify/${credentials.verificationCode}`, AuthService.getHttpOptions());
  }

  public loginSocial(user: LoginSocialCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login-social', user, AuthService.getHttpOptions());
  }

  public logout(token: UserTokenState | ""): Observable<Object> {
    return this.http.post(this.authUrl + '/logout', token, AuthService.getHttpOptions());
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/currently-logged-user', AuthService.getHttpOptions());
  }

  public static getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
        'Content-Type': 'application/json',
      })
    };
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    console.log(token);
    return token !== null;
  }
}
