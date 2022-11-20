import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {CarTypeGetResponse} from "../model/CarTypeGetResponse";

@Injectable({
  providedIn: 'root'
})
export class CarTypeService {

  private readonly carTypeUrl: string;

  constructor(private http: HttpClient) {
    this.carTypeUrl = 'http://localhost:8000/car-type';
  }

  public getCarType(name: String): Observable<CarTypeGetResponse> {
    return this.http.get<CarTypeGetResponse>(this.carTypeUrl + `/${name}`, CarTypeService.getHttpOptions());
  }

  public static getHttpOptions() {
    console.log("MILAN")
    console.log(localStorage.getItem('token'))
    return {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*',
        'Authorization': localStorage.getItem('token') || 'authkey',
      })
    };
  }
}
