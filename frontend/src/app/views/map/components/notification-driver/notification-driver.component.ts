import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/User";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {DriverNotification} from "../../../../model/DriverNotification";
import {DriverNotificationService} from "../../../../services/driver-notification.service";
import {RouteDTO} from "../../../../model/RouteDTO";
import {MatDialog} from "@angular/material/dialog";
import {RideDetails} from "../../../../model/RideDetails";
import {
  ReasonForRideCancellationComponent
} from "../reason-for-ride-cancelation/reason-for-ride-cancellation.component";
import {Place} from "../../../../model/Position";

@Component({
  selector: 'app-notification-driver',
  templateUrl: './notification-driver.component.html',
  styleUrls: ['./notification-driver.component.css']
})
export class NotificationDriverComponent implements OnInit {

  @Input() loggedUser: User;
  private stompClient: any;
  notifications: DriverNotification[];

  constructor(public dialog: MatDialog, private driverNotificationService: DriverNotificationService) {
    this.notifications = []
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection()
    this.initDriverNotifications()
  }

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openDriverNotificationSocket();
    });
  }

  openDriverNotificationSocket() {
    this.stompClient.subscribe("/notify-driver/new-ride-" + this.loggedUser.email, (message: any) => {
      let notification: DriverNotification = JSON.parse(message.body)
      this.notifications.push(notification)
    })
    this.stompClient.subscribe("/notify-driver/start-ride-" + this.loggedUser.email, (message: any) => {
      let notification: DriverNotification = JSON.parse(message.body)
      this.notifications.push(notification)
    })

    // this.stompClient.subscribe("/map-updates/update-route-for-selected-car", (message: any) => {
    //   this.createRouteForSelectedCar(JSON.parse(message.body))
    //   console.log(message)
    //   // this.sideNav.notify(JSON.parse(message.body).customers)
    // })
    // this.stompClient.subscribe("/notify/split-fare-" + this.loggedUser.email, (message: any) => {
    //   console.log(message)
    //   console.log("Stigao sam na drugu stranu")
    //   this.sideNav.notify(JSON.parse(message.body))
    // })

  }

  private initDriverNotifications() {
    this.driverNotificationService.getCurrent(this.loggedUser.email).subscribe((res: DriverNotification[]) => {
      this.notifications = this.notifications.concat(res);
    })
  }

  public removeDriverNotifications(driverNotifications: DriverNotification[]) {
    driverNotifications.forEach((notificationForDelete: DriverNotification) => {
      this.notifications = this.notifications.filter(notification => notification.id !== notificationForDelete.id)
    })
  }

  getRideCheckpoints(checkPoints: Place[]) {
    let checkPointDisplay = ""
    checkPoints.forEach((checkPoint, index) => {
      if (index === 0) {
        checkPointDisplay = checkPoint.name
      } else {
        checkPointDisplay = checkPointDisplay + " -> " + checkPoint.name
      }
    })
    return checkPointDisplay
  }


  startRide(ride: RideDetails) {

  }

  cancelRideTechnicalOrHealthProblem(ride: RideDetails) {
    this.openReasonDialog(ride, true)
  }

  cancelRide(ride: RideDetails) {
    this.openReasonDialog(ride, false)
  }

  openReasonDialog(ride: RideDetails, shouldSetDriverInactive: boolean) {
    const dialogRef = this.dialog.open(ReasonForRideCancellationComponent, {
      data: {
        ride, shouldSetDriverInactive
      }
    })
  }
}
