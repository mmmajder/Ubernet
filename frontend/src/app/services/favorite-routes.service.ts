import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {FavoriteRouteItem, FavoriteRouteRequest} from "../model/FavoriteRoute";

@Injectable({
  providedIn: 'root'
})
export class FavoriteRoutesService {

  private readonly favoriteRoutesUrl: string;

  constructor(private http: HttpClient) {
    this.favoriteRoutesUrl = 'http://localhost:8000/favoriteRoute/';
  }

  public getFavoriteRoutes(customerEmail: string): Observable<FavoriteRouteItem[]> {
    return this.http.get<FavoriteRouteItem[]>(this.favoriteRoutesUrl + "getFavoriteRoutes/" + customerEmail, AuthService.getHttpOptions());
  }

  public isRouteFavorite(customerEmail: string, rideId: number): Observable<boolean> {
    let body = new FavoriteRouteRequest(customerEmail, rideId);
    return this.http.post<boolean>(this.favoriteRoutesUrl + "isRouteFavorite", body, AuthService.getHttpOptions());
  }

  public addToFavoriteRoutes(customerEmail: string, rideId: number): Observable<void> {
    let body = new FavoriteRouteRequest(customerEmail, rideId);
    return this.http.post<void>(this.favoriteRoutesUrl + "addToFavoriteRoutes", body, AuthService.getHttpOptions());
  }

  public removeFromFavoriteRoutes(customerEmail: string, rideId: number): Observable<void> {
    let body = new FavoriteRouteRequest(customerEmail, rideId);
    return this.http.post<void>(this.favoriteRoutesUrl + "removeFromFavoriteRoutes", body, AuthService.getHttpOptions());
  }
}
