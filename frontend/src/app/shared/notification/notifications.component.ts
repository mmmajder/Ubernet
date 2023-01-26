import {Component, Input, OnInit} from '@angular/core';
import {NotificationDTO} from "../../model/NotificationDTO";
import {NotificationService} from "../../services/notification.service";
import {Store} from "@ngxs/store";
import {User} from "../../model/User";
import {MatDialog} from "@angular/material/dialog";
import {
  RideSplitFareDialogComponent
} from "../../views/request-ride-accept/ride-split-fare-dialog/ride-split-fare-dialog.component";
import {SidenavComponent} from "../sidenav/sidenav/sidenav.component";
import {
  ReportDriverDialogComponent
} from "../../views/customer/components/report-driver-dialog/report-driver-dialog.component";

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  @Input() notifications: NotificationDTO[];
  user: User;

  constructor(private dialog: MatDialog, private notificationService: NotificationService, private store: Store) {
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

  notificationClicked(event: any) {
    const notificationId = event.target.attributes.id.value
    this.notificationService.getNotificationById(notificationId).subscribe((clickedNotification) => {
      console.log(clickedNotification)
      if (clickedNotification.type === "SPLIT_FARE") {
        this.dialog.open(RideSplitFareDialogComponent, {data: clickedNotification});
      } else if (clickedNotification.type === "DRIVER_INCONSISTENCY") {
        this.dialog.open(ReportDriverDialogComponent, {data: clickedNotification});
      }
    })
  }
}
