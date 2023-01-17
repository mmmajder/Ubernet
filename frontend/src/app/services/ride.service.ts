import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {RideCreate} from "../model/RideCreate";
import {Ride} from "../model/Ride";
import {PositionInTime} from "../model/PositionInTime";
import {Position} from "../model/Position";
import {RideDetails} from "../model/RideDetails";
import {RideDTO} from "../model/RideDTO";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  private readonly rideUrl: string;

  constructor(private http: HttpClient) {
    this.rideUrl = 'http://localhost:8000/ride';
  }

  public createRideRequest(ride: RideCreate): Observable<Ride> {
    return this.http.post<Ride>(this.rideUrl + "/create", ride, RideService.getHttpOptions());
  }

  public createRouteForSelectedCar(ride: RideCreate, carId: number): Observable<void> {
    return this.http.post<void>(this.rideUrl + "/update-car-route/" + carId, ride, RideService.getHttpOptions());
  }

  public acceptRequestSplitFare(url: string):Observable<any> {
    return this.http.put<any>(this.rideUrl + "/accept-request-split-fare/" + url, RideService.getHttpOptions());
  }

  public getById(id: number):Observable<RideDTO> {
    return this.http.get<RideDTO>(this.rideUrl + "/" + id, RideService.getHttpOptions());
  }

  public getLastPosition(positionsInTime:PositionInTime[]):Position {
    let lastPosition = positionsInTime[0]
    positionsInTime.forEach((positionsInTime:PositionInTime) => {
      if (positionsInTime.secondsPassed>lastPosition.secondsPassed)
        lastPosition = positionsInTime;
    })
    return lastPosition.position;
  }

  public static getHttpOptions() {
    console.log(localStorage.getItem('token'))
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }



}
