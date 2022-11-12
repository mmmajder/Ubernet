import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {LoginResponseDto, UserTokenState} from "../model/LoginResponseDto";
import {LoginCredentials} from "../model/LoginCredentials";
import {User} from "../model/User";
import {AuthState} from "../store/states/auth.state";

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
      'Authorization': 'authkey',
    })
  };
  httpOptionsLogged = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': localStorage.getItem('token') || "",
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
    return this.http.get<User>(this.currentlyLoggedUrl, this.httpOptionsLogged);
  }

  /*
    public findById(id: string): Observable<Restaurant> {
      return this.http.get<Restaurant>(this.createRestaurantUrl + '/' + id, this.httpOptions);
    }

    public createRestaurant(restaurant: RestaurantRequestModel): Observable<Restaurant> {
      let body = {
        "name": restaurant.name,
        "address": restaurant.address,
        "phone": restaurant.phone,
        "email": restaurant.email,
        "deliveryRate": restaurant.deliveryRate,
        "menuItems": []
      }
      return this.http.post<Restaurant>(this.createRestaurantUrl, body, this.httpOptions);
    }

    public removeMenuItem(id: string, name: string) {
      return this.http.post<Restaurant>(this.createRestaurantUrl + '/' + id + '/remove-menu-item', name, this.httpOptions);
    }

    public addMenuItem(menuItem: MenuItem): Observable<MenuItem> {
      let body = {
        "name": menuItem.name,
        "description": menuItem.description,
        "price": menuItem.price
      }
      return this.http.post<MenuItem>(this.createRestaurantUrl + '/' + menuItem.id + '/menu-item', body, this.httpOptions);
    }*/
}
