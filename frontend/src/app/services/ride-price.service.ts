import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class RidePayService {
  private readonly ridePriceUrl: string;

  constructor(private http: HttpClient) {
    this.ridePriceUrl = 'http://localhost:8000/ride-price';
  }

  public calculatePrice(estimatedLengthInKm: number, carType: string): Observable<number> {
    let body = {
      "estimatedLengthInKm": estimatedLengthInKm,
      "carType": carType
    }
    return this.http.put<number>(this.ridePriceUrl, body, AuthService.getHttpOptions());
  }
}
