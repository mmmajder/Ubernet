import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";
import {LoginSocialCredentials} from "../model/LoginSocialCredentials";
import {RegisterCredentials} from "../model/RegisterCredentials";
import {VerifyCredentials} from "../model/VerifyCredentials";
import {SetPasswordDTO} from "../model/SetPasswordDTO";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8000/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    const body = {
      "email": user.email,
      "password": user.password
    }
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', body, AuthService.getHttpOptions());
  }

  public register(customer: RegisterCredentials): Observable<string> {
    return this.http.post<string>(this.authUrl + '/register', customer, AuthService.getHttpOptions());
  }

  public verify(credentials: string): Observable<void> {
    return this.http.get<void>(this.authUrl + `/verify/` + credentials, AuthService.getHttpOptions());
  }

  public loginSocial(user: LoginSocialCredentials): Observable<LoginResponseDto> {
    const body = {
      "email": user.email,
      "authToken": user.authToken,
      "firstName": user.firstName,
      "id": user.id,
      "idToken": user.idToken,
      "lastName": user.lastName,
      "name": user.name,
      "photoUrl": user.photoUrl,
      "provider": user.provider
    }
    return this.http.post<LoginResponseDto>(this.authUrl + '/login-social', body, AuthService.getHttpOptions());
  }

  public logout(token: UserTokenState): Observable<Object> {
    return this.http.post(this.authUrl + '/logout/' + token.accessToken, AuthService.getHttpOptions());
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
    console.log(localStorage.getItem('token'))
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
        'Content-Type': 'application/json',
      })
    };
  }



}
