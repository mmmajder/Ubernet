import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {LeafletRoute} from "../model/LeafletRoute";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class RideAlternativeService {

  private readonly rideAlternativeUrl: string;

  constructor(private http: HttpClient) {
    this.rideAlternativeUrl = 'http://localhost:8000/ride-alternatives';
  }

  createRideAlternatives(rideId: number, allAlternatives: LeafletRoute[][]): Observable<void> {
    return this.http.post<void>(this.rideAlternativeUrl + "/" + rideId, allAlternatives, AuthService.getHttpOptions());
  }
}
