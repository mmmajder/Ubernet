import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {FavoriteRouteItem, FavoriteRouteRequest} from "../model/FavoriteRoute";
import {RideToRate} from "../model/RideToRate";
import {CreateReview} from "../model/Review";

@Injectable({
  providedIn: 'root'
})
export class RatingsService {

  private readonly ratingsUrl: string;

  constructor(private http: HttpClient) {
    this.ratingsUrl = 'http://localhost:8000/review/';
  }

  public getRidesToRate(customerEmail: string): Observable<RideToRate[]> {
    return this.http.get<RideToRate[]>(this.ratingsUrl + "getRidesToRate/" + customerEmail, AuthService.getHttpOptions());
  }

  public rateRide(createReview: CreateReview): Observable<boolean> {
    return this.http.post<boolean>(this.ratingsUrl, createReview, AuthService.getHttpOptions());
  }
}
