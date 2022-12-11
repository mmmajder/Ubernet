import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from "rxjs";
import {Customer, User, UserDTO} from "../model/User";
import {AuthService} from "./auth.service";
import {PasswordChangeInfo} from "../model/PasswordChangeInfo";
import {Ride} from "../model/Ride";

@Injectable({
  providedIn: 'root'
})
export class RidesHistoryService {

  private readonly ridesUrl: string;

  constructor(private http: HttpClient) {
    this.ridesUrl = 'http://localhost:8000/rides';
  }

  public getRides(filter = '', driverEmail = '', customerEmail = '', sortOrder = 'asc', pageNumber = 0, pageSize = 10): Observable<Ride[]> {
    return this.http.get<Ride[]>(this.ridesUrl, {
      params: new HttpParams()
        .set('filter', filter)
        .set('sortOrder', sortOrder)
        .set('driverEmail', driverEmail)
        .set('customerEmail', customerEmail)
        .set('pageNumber', pageNumber.toString())
        .set('pageSize', pageSize.toString())
    });
  }

}
