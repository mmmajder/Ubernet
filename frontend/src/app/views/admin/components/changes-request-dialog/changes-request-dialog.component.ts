import {Component, OnInit} from '@angular/core';
import {DriversService} from "../../../../services/drivers.service";
import {ProfileChangesRequest} from "../../../../model/RegisterCredentials";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AdminService} from "../../../../services/admin.service";

@Component({
  selector: 'app-changes-request-dialog',
  templateUrl: './changes-request-dialog.component.html',
  styleUrls: ['./changes-request-dialog.component.css']
})
export class ChangesRequestDialogComponent implements OnInit {
  driverEmail = "";
  request: ProfileChangesRequest = new ProfileChangesRequest()
  showOnlyChanges = false;

  constructor(private driversService: DriversService, private adminService: AdminService, private _snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.driversService.getProfileChangesRequest(this.driverEmail).subscribe({
      next: value => {
        this.request = value;
      }
    })
  }

  formatDate(requestTime: string) {
    const splited = requestTime.split(',');
    return splited[2] + "." + splited[1] + "." + splited[0] + ". at " + splited[3] + ":" + splited[4];
  }

  acceptRequest(accepted: boolean) {
    this.adminService.acceptProfileChange(this.driverEmail, accepted).subscribe({
      next: () => this._snackBar.open("Action was a success.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      }),
      error: () => {
        this._snackBar.open("Action was a success.", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        });
      }
    })
  }
}
