import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {CancelRideRequest} from "../model/CancelRideRequest";
import {RideDenial} from "../model/RideDenial";

@Injectable({
  providedIn: 'root'
})
export class RideDenialService {

  private readonly rideDenialUrl: string;

  constructor(private http: HttpClient) {
    this.rideDenialUrl = 'http://localhost:8000/ride-denial';
  }

  public createRideDenial(cancelRideRequest: CancelRideRequest, rideId: number): Observable<RideDenial> {
    return this.http.post<RideDenial>(this.rideDenialUrl + "/" + rideId, cancelRideRequest, RideDenialService.getHttpOptions());
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
