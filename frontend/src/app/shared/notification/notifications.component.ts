import {Component, Input, OnInit} from '@angular/core';
import {NotificationDTO} from "../../model/NotificationDTO";
import {NotificationService} from "../../services/notification.service";
import {Store} from "@ngxs/store";
import {User} from "../../model/User";
import {MatDialog} from "@angular/material/dialog";
import {
  RideSplitFareDialogComponent
} from "../../views/request-ride-accept/ride-split-fare-dialog/ride-split-fare-dialog.component";
import {
  ReportDriverDialogComponent
} from "../../views/customer/components/report-driver-dialog/report-driver-dialog.component";
import {
  DriversProfileDialogComponent
} from "../../views/admin/components/drivers-profile-dialog/drivers-profile-dialog.component";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  @Input() notifications: NotificationDTO[];
  user: User;
  photo: string;

  constructor(private driversProfile: MatDialog, private dialog: MatDialog, private notificationService: NotificationService, private store: Store) {
    this.photo = '../../assets/default-profile-picture.jpg';
  }

  ngOnInit(): void {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.user = user;
        if (this.user.role === "CUSTOMER")
          this.notificationService.getNotifications(user.email).subscribe((notifications) => {
            this.notifications = notifications.reverse()
            console.log(this.notifications)
          })
      }
    })
  }

  notificationClicked(id: any) {
    const notificationId = id
    this.notificationService.getNotificationById(notificationId).subscribe((clickedNotification) => {
      console.log(clickedNotification)
      if (clickedNotification.type === "SPLIT_FARE") {
        this.dialog.open(RideSplitFareDialogComponent, {data: clickedNotification});
      } else if (clickedNotification.type === "DRIVER_INCONSISTENCY") {
        this.dialog.open(ReportDriverDialogComponent, {data: clickedNotification});
      }
    })
  }

  openDriverInfoPopup(driverEmail: string) {
    const dialogRef = this.driversProfile.open(DriversProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = driverEmail;
  }
}
