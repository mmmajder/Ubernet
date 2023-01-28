import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {RideCreate} from "../model/RideCreate";
import {PositionInTime} from "../model/PositionInTime";
import {Position} from "../model/Position";
import {RideDetails} from "../model/RideDetails";
import {RideDTO} from "../model/RideDTO";
import {CurrentRide} from "../model/CurrentRide";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private readonly rideUrl: string;

  constructor(private http: HttpClient) {
    this.rideUrl = 'http://localhost:8000/ride';
  }

  public createRideRequest(ride: RideCreate): Observable<RideDetails> {
    return this.http.post<RideDetails>(this.rideUrl + "/create", ride, AuthService.getHttpOptions());
  }

  public createRouteForSelectedCar(ride: RideCreate, carId: number): Observable<void> {
    return this.http.post<void>(this.rideUrl + "/update-car-route/" + carId, ride, AuthService.getHttpOptions());
  }

  public acceptRequestSplitFare(url: string): Observable<any> {
    return this.http.put<any>(this.rideUrl + "/accept-request-split-fare/" + url, AuthService.getHttpOptions());
  }

  public getById(id: number): Observable<RideDTO> {
    return this.http.get<RideDTO>(this.rideUrl + "/" + id, AuthService.getHttpOptions());
  }

  public startRide(id: number): Observable<RideDetails> {
    return this.http.put<RideDetails>(this.rideUrl + "/start-ride/" + id, AuthService.getHttpOptions());
  }

  public endRide(id: number): Observable<RideDetails> {
    return this.http.put<RideDetails>(this.rideUrl + "/end-ride/" + id, AuthService.getHttpOptions());
  }

  findScheduledRouteForClient(email: string): Observable<CurrentRide> {
    return this.http.get<CurrentRide>(this.rideUrl + "/find-scheduled-route-navigation-client/" + email, AuthService.getHttpOptions());
  }

  public getLastPosition(positionsInTime: PositionInTime[]): Position {
    return positionsInTime[positionsInTime.length - 1].position
  }
}
