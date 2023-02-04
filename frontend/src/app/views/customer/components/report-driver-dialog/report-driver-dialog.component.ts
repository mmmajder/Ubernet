import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {NotificationDTO} from "../../../../model/NotificationDTO";
import {DriverInconsistencyService} from "../../../../services/driver-inconsistency.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-report-driver-dialog',
  templateUrl: './report-driver-dialog.component.html',
  styleUrls: ['./report-driver-dialog.component.css']
})
export class ReportDriverDialogComponent {

  notification: NotificationDTO

  constructor(private _snackBar: MatSnackBar, @Inject(MAT_DIALOG_DATA) public data: NotificationDTO, private driverInconsistencyService: DriverInconsistencyService) {
    this.notification = data
  }

  accept() {
    this.driverInconsistencyService.create(this.notification.receiverEmail, this.notification.rideId).subscribe(
      {
        next: () => {
          this._snackBar.open("Successfully reported the driver", '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        },
        error: (res) => {
          this._snackBar.open(res.error, '', {
            duration: 3000,
            panelClass: ['snack-bar']
          })
        }
      }
    )
  }
}
