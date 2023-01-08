import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {Ride} from "../model/Ride";
import {AuthService} from "./auth.service";
import {RideDetails} from "../model/RideDetails";

export interface Page {
  content: Ride[];
  totalPages: number;
  size: number;
  totalElements: number;
}

@Injectable({
  providedIn: 'root'
})
export class RidesHistoryService {

  private readonly ridesUrl: string;

  constructor(private http: HttpClient) {
    this.ridesUrl = 'http://localhost:8000/ridesHistory/';
  }

  public getRides(driverEmail = '', customerEmail = '', sortKind = 'start', sortOrder = 'desc', pageNumber = 0, pageSize = 10): Observable<Page> {
    let body = {
      'sortKind': sortKind,
      'sortOrder': sortOrder,
      'driverEmail': driverEmail,
      'customerEmail': customerEmail,
      'pageNumber': pageNumber,
      'pageSize': pageSize
    }
    return this.http.post<Page>(this.ridesUrl + "getRides", body, AuthService.getHttpOptions());
  }

  public getRideById(id: number): Observable<RideDetails> {
    return this.http.get<RideDetails>(this.ridesUrl + "getRide/" + id, AuthService.getHttpOptions());
  }
}
