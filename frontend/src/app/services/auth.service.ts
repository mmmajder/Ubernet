import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";
import {LoginSocialCredentials} from "../model/LoginSocialCredentials";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly authUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': localStorage.getItem('token') || 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.authUrl = 'http://localhost:8000/auth';
  }

  public login(user: LoginCredentials): Observable<LoginResponseDto> {
    let body = {
      "email": user.email,
      "password": user.password
    }
    console.log(body)
    return this.http.post<LoginResponseDto>(this.authUrl + '/login', body, this.httpOptions);
  }

  public logout(token: UserTokenState | ""): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(this.authUrl + '/logout', token, this.httpOptions);
  }

  public getCurrentlyLoggedUser(): Observable<User> {
    return this.http.get<User>(this.authUrl + '/currently-logged-user', this.httpOptions);
  }

  public loginSocial(user: LoginSocialCredentials): Observable<LoginResponseDto> {
    let body = {
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
    console.log(body)
    return this.http.post<LoginResponseDto>(this.authUrl + '/login-social', body, this.httpOptions);
  }
}
