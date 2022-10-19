import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {
  RestaurantRequestModel
} from "../views/restaurants/components/create-restaurant-dialog/create-restaurant-dialog.component";

export class Restaurant {
  id!: string;
  name!: string;
  address!: string;
  phone!: string;
  email!: string;
  deliveryRate: number = 0;
  menuItems: MenuItem[] = [];
}

export class MenuItem {
  id!: string;
  name!: string;
  price: number = 0;
  description: string = "";
}

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private readonly restaurantsUrl: string;
  private readonly createRestaurantUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.restaurantsUrl = 'http://localhost:8081/api/back-office/restaurants';
    this.createRestaurantUrl = 'http://localhost:8081/api/back-office/restaurant';
  }

  public findAll(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(this.restaurantsUrl, this.httpOptions);
  }

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
  }
}
