import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {CancelRideRequest} from "../model/CancelRideRequest";
import {RideDenial} from "../model/RideDenial";
import {LeafletRoute} from "../model/LeafletRoute";

@Injectable({
  providedIn: 'root'
})
export class RideAlternativeService {

  private readonly rideAlternativeUrl: string;

  constructor(private http: HttpClient) {
    this.rideAlternativeUrl = 'http://localhost:8000/ride-alternatives';
  }

  createRideAlternatives(rideId: number, allAlternatives: LeafletRoute[][]):Observable<void> {
    return this.http.post<void>(this.rideAlternativeUrl + "/" + rideId, allAlternatives, RideAlternativeService.getHttpOptions());
  }

  public static getHttpOptions() {
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }
}
