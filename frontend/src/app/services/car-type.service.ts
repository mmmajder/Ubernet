import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {CarTypeGetResponse} from "../model/CarTypeGetResponse";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class CarTypeService {

  private readonly carTypeUrl: string;

  constructor(private http: HttpClient) {
    this.carTypeUrl = 'http://localhost:8000/car-type';
  }

  public getCarTypes(): Observable<CarTypeGetResponse[]> {
    return this.http.get<CarTypeGetResponse[]>(this.carTypeUrl + "/all", AuthService.getHttpOptions());
  }
}
