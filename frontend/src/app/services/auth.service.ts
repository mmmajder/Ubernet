import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";
import {LoginSocialCredentials} from "../model/LoginSocialCredentials";
import {RegisterCredentials, RegisterDriverCredentials} from "../model/RegisterCredentials";
import {SetPasswordDTO} from "../model/SetPasswordDTO";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;
  facebookFlag: boolean = false;

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8000/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', user, AuthService.getHttpOptions());
  }

  public register(customer: RegisterCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register', customer, AuthService.getHttpOptions());
  }

  public registerDriver(user: RegisterDriverCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/registerDriver', user, AuthService.getHttpOptions());
  }

  public verify(credentials: string): Observable<void> {
    return this.http.get<void>(this.authUrl + `/verify/` + credentials, AuthService.getHttpOptions());
  }

  public loginSocial(user: LoginSocialCredentials): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/login-social', user, AuthService.getHttpOptions());
  }

  public logout(token: UserTokenState): Observable<Object> {
    console.log(token)
    return this.http.post(this.authUrl + '/logout/' + (localStorage.getItem('token') as string).split(" ")[1], AuthService.getHttpOptions());
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/currently-logged-user', AuthService.getHttpOptions());
  }

  public forgotPassword(email: string): Observable<void> {
    return this.http.put<void>(this.authUrl + '/reset-password/' + email, AuthService.getHttpOptions());
  }

  public setPassword(resetPasswordCode: string, setPasswordDTO: SetPasswordDTO): Observable<void> {
    return this.http.put<void>(this.authUrl + '/set-password/' + resetPasswordCode, setPasswordDTO, AuthService.getHttpOptions());
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
