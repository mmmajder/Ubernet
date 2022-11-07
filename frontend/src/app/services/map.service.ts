import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {Restaurant} from "./restaurant.service";

class Position {
  id: number;
  x: number;
  y: number;
}

class ActiveCarResponse {
  carId:number;
  driverEmail:string;
  destinations:Position[];
  currentPosition: Position;
}

// class UserTokenState {
//   accessToken!: string;
//   expiredIn!: number;
// }
//
// enum UserRole {
//   ADMIN,
//   DRIVER,
//   CUSTOMER
// }
//
// class LoginResponseDTO {
//   userRole!: UserRole;
//   token!: UserTokenState;
// }
//
// class User {
//   email!: string;
//   password!: string;
// }

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private readonly mapUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.mapUrl = 'http://localhost:8000/car';
  }

  public getActiveCars(): Observable<ActiveCarResponse> {
    return this.http.get<ActiveCarResponse>(this.mapUrl + "/active", this.httpOptions);
  }

  public getNewPositionOfCar(carId:number) {
    return this.http.get<ActiveCarResponse>(this.mapUrl + "/position/" + carId, this.httpOptions);
  }

  public setNewPositionOfCar(carId:number) {
    return this.http.put<ActiveCarResponse>(this.mapUrl + "/position/", carId, this.httpOptions);
  }

  // public removeMenuItem(id: string, name: string) {
  //   return this.http.post<Restaurant>(this.createRestaurantUrl + '/' + id + '/remove-menu-item', name, this.httpOptions);
  // }

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
