import {Component} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {Chart, registerables} from "chart.js";
import {ReportService} from "../../../services/reports.service";
import {CurrentlyLogged} from "../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {UserRole} from "../../../model/UserRole";
import {ReportRequest, ReportResponse} from "../../../model/ReportRequest";
import {MatSnackBar} from "@angular/material/snack-bar";

Chart.register(...registerables);

@Component({
  selector: 'app-analytics-container',
  templateUrl: './analytics-container.component.html',
  styleUrls: ['./analytics-container.component.css']
})
export class AnalyticsContainerComponent {
  chart1: Chart;
  chart2: Chart;
  chart3: Chart;
  userRole: UserRole;
  userEmail: string;

  averageSpent = 0;
  totalSpent = 0;

  startDate: Date;
  endDate: Date;

  campaignOne = new FormGroup({
    start: new FormControl(new Date()),
    end: new FormControl(new Date()),
  });

  constructor(private reportService: ReportService, private store: Store, private _snackBar: MatSnackBar) {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.userRole = resp.loggedUser.role;
        this.userEmail = resp.loggedUser.email;
      },
      error: () => this.userRole = UserRole.ADMIN
    });
  }

  getData() {
    if (this.startDate && this.endDate) {
      const reportRequest: ReportRequest = {
        'email': this.userEmail,
        'startDate': this.formatDate(this.startDate),
        'endDate': this.formatDate(this.endDate)
      };
      if (this.userRole == UserRole.DRIVER) {
        this.reportService.generateReportForDriver(reportRequest).subscribe({
          next: value => this.updateCharts(value),
          error: () => this.showErrorSnackBar()
        })
      } else if (this.userRole == UserRole.CUSTOMER) {
        this.reportService.generateReportForCustomer(reportRequest).subscribe({
          next: value => this.updateCharts(value),
          error: () => this.showErrorSnackBar()
        })
      } else {
        this.reportService.generateReportForAdmin(reportRequest).subscribe({
          next: value => this.updateCharts(value),
          error: () => this.showErrorSnackBar()
        })
      }
    }
  }

  showErrorSnackBar() {
    this._snackBar.open("Couldn't generate report.", '', {
      duration: 3000,
      panelClass: ['snack-bar']
    });
  }

  updateCharts(report: ReportResponse) {
    this.averageSpent = report.averageMoneyPerDay;
    this.totalSpent = report.totalSum;

    if (this.chart1)
      this.chart1.destroy();
    this.chart1 = this.getNewChart('#272d2d', report.numberOfKm, 'Number of drived kilometers per day', 'myChart1');

    if (this.chart2)
      this.chart2.destroy();
    this.chart2 = this.getNewChart('#3f51b5', report.numberOfRides, 'Number of rides per day', 'myChart2');

    if (this.chart3)
      this.chart3.destroy();
    let label = 'Raised money per day in tokens';
    if (this.userRole == UserRole.CUSTOMER)
      label = 'Spent money per day in tokens';
    this.chart3 = this.getNewChart('#F0C000', report.money, label, 'myChart3');
  }

  formatDate(date: Date) {
    const year = date.getFullYear();

    let month = (1 + date.getMonth()).toString();
    month = month.length > 1 ? month : '0' + month;

    let day = date.getDate().toString();
    day = day.length > 1 ? day : '0' + day;

    return month + '/' + day + '/' + year;
  }

  getNewChart(color: string, data: number[], label: string, item: string) {
    return new Chart(item, {
      type: 'bar',
      data: {
        labels: this.getLabels(this.startDate, this.endDate),
        datasets: [
          {
            label: label,
            data: data,
            borderColor: color,
            backgroundColor: color,
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  getLabels(startDate: Date, endDate: Date): string[] {
    const arr = [];
    for (let dt = new Date(startDate); dt <= new Date(endDate); dt.setDate(dt.getDate() + 1)) {
      arr.push(new Date(dt).toDateString());
    }
    return arr;
  }
}
