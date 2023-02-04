import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private readonly driverUrl: string;

  constructor(private http: HttpClient) {
    this.driverUrl = 'http://localhost:8000/admin';
  }

  public acceptProfileChange(driverEmail: string, accepted: boolean): Observable<string> {
    const body = {
      'driverEmail': driverEmail,
      'accepted': accepted
    }
    return this.http.post<string>(this.driverUrl + "/acceptProfileChange", body, AuthService.getHttpOptions());
  }
}
