import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
import {LeafletRoute} from "../model/LeafletRoute";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private readonly carUrl: string;
  private readonly mapUrl: string

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.carUrl = 'http://localhost:8000/car';
    this.mapUrl = 'http://localhost:8000/map'
  }

  public getActiveCars(): Observable<ActiveCarResponse[]> {
    return this.http.get<ActiveCarResponse[]>(this.carUrl + "/active", this.httpOptions);
  }

  public optimizeRouteByPrice(selectedRoute: LeafletRoute[][]): Observable<LeafletRoute[]> {
    return this.http.put<LeafletRoute[]>(this.mapUrl + "/optimize-by-price", selectedRoute, this.httpOptions);
  }

  public optimizeRouteByTime(selectedRoute: LeafletRoute[][]): Observable<LeafletRoute[]> {
    return this.http.put<LeafletRoute[]>(this.mapUrl + "/optimize-by-time", selectedRoute, this.httpOptions);
  }

  findAddress(address: string) {
    const url = "https://nominatim.openstreetmap.org/search?format=json&limit=3&q=" + address
    return this.http.get(url);
  }

}
