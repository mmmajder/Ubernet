import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.userUrl = 'http://localhost:8000/user/';
  }

  public getUser(email: string): Observable<User> {
    return this.http.get<User>(this.userUrl + "?email=" + email, this.httpOptions);
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
