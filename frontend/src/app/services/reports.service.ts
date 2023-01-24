import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {ReportRequest, ReportResponse} from "../model/ReportRequest";

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly reportUrl: string;

  constructor(private http: HttpClient) {
    this.reportUrl = 'http://localhost:8000/reports';
  }

  public generateReportForDriver(reportRequest: ReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(this.reportUrl + "/getDriverReport", reportRequest, AuthService.getHttpOptions());
  }

  public generateReportForAdmin(reportRequest: ReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(this.reportUrl + "/getAdminReport", reportRequest, AuthService.getHttpOptions());
  }

  public generateReportForCustomer(reportRequest: ReportRequest): Observable<ReportResponse> {
    return this.http.post<ReportResponse>(this.reportUrl + "/getCustomerReport", reportRequest, AuthService.getHttpOptions());
  }
}
