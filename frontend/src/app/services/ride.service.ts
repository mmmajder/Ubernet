import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {RideCreate} from "../model/RideCreate";
import {PositionInTime} from "../model/PositionInTime";
import {Position} from "../model/Position";
import {RideDetails} from "../model/RideDetails";
import {RideDTO} from "../model/RideDTO";
import {RouteDTO} from "../model/RouteDTO";
import {CurrentRide} from "../model/CurrentRide";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private readonly rideUrl: string;

  constructor(private http: HttpClient) {
    this.rideUrl = 'http://localhost:8000/ride';
  }

  public createRideRequest(ride: RideCreate): Observable<RideDetails> {
    return this.http.post<RideDetails>(this.rideUrl + "/create", ride, RideService.getHttpOptions());
  }

  public createRouteForSelectedCar(ride: RideCreate, carId: number): Observable<void> {
    return this.http.post<void>(this.rideUrl + "/update-car-route/" + carId, ride, RideService.getHttpOptions());
  }

  public acceptRequestSplitFare(url: string): Observable<any> {
    return this.http.put<any>(this.rideUrl + "/accept-request-split-fare/" + url, RideService.getHttpOptions());
  }

  public getById(id: number): Observable<RideDTO> {
    return this.http.get<RideDTO>(this.rideUrl + "/" + id, RideService.getHttpOptions());
  }

  public startRide(id: number): Observable<RideDetails> {
    return this.http.put<RideDetails>(this.rideUrl + "/start-ride/" + id, RideService.getHttpOptions());
  }

  public endRide(id: number): Observable<RideDetails> {
    return this.http.put<RideDetails>(this.rideUrl + "/end-ride/" + id, RideService.getHttpOptions());
  }

  findScheduledRouteForClient(email: string): Observable<CurrentRide> {
    return this.http.get<CurrentRide>(this.rideUrl + "/find-scheduled-route-navigation-client/" + email, RideService.getHttpOptions());
  }

  public getLastPosition(positionsInTime: PositionInTime[]): Position {
    return positionsInTime[positionsInTime.length - 1].position
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
