import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {User} from "../../../../model/User";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {DriverNotification} from "../../../../model/DriverNotification";
import {DriverNotificationService} from "../../../../services/driver-notification.service";
import {MatDialog} from "@angular/material/dialog";
import {RideDetails} from "../../../../model/RideDetails";
import {
  ReasonForRideCancellationComponent
} from "../reason-for-ride-cancelation/reason-for-ride-cancellation.component";
import {Place} from "../../../../model/Position";
import {RideService} from "../../../../services/ride.service";

@Component({
  selector: 'app-notification-driver',
  templateUrl: './notification-driver.component.html',
  styleUrls: ['./notification-driver.component.css']
})
export class NotificationDriverComponent implements OnInit {

  @Input() loggedUser: User;
  @Output() updateRouteDisplay = new EventEmitter<void>();
  private stompClient: any;
  notifications: DriverNotification[];

  constructor(public dialog: MatDialog, private driverNotificationService: DriverNotificationService, private rideService: RideService) {
    this.notifications = []
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection()
    this.initDriverNotifications()
  }

  initializeWebSocketConnection() {
    const ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openDriverNotificationSocket();
    });
  }

  openDriverNotificationSocket() {
    this.stompClient.subscribe("/notify-driver/new-ride-" + this.loggedUser.email, (message: any) => {
      const notification: DriverNotification = JSON.parse(message.body)
      this.notifications.push(notification)
    })
    this.stompClient.subscribe("/notify-driver/start-ride-" + this.loggedUser.email, (message: any) => {
      const notification: DriverNotification = JSON.parse(message.body)
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

  public updateNotificationStartRide(notification: DriverNotification) {
    if (this.notifications.filter(e => e.id === notification.id).length === 0)
      this.notifications.push(notification)
    // console.log(ride)
    // console.log(this.notifications)
    // this.notifications = this.notifications.filter(notification => notification.ride.id !== ride.id)
  }

  updateNotificationEndRide(endRideNotification: DriverNotification) {
    this.notifications.push(endRideNotification);
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
    this.rideService.startRide(ride.id).subscribe((ride: RideDetails) => {
      console.log(ride)
      this.notifications = this.notifications.filter(item => item.ride.id !== ride.id)
      this.updateRouteDisplay.emit()
    })

  }

  cancelRideTechnicalOrHealthProblem(ride: RideDetails) {
    this.openReasonDialog(ride, true)
    this.updateRouteDisplay.emit()
  }

  cancelRide(ride: RideDetails) {
    this.openReasonDialog(ride, false)
    this.updateRouteDisplay.emit()
  }

  openReasonDialog(ride: RideDetails, shouldSetDriverInactive: boolean) {
    this.dialog.open(ReasonForRideCancellationComponent, {
      data: {
        ride, shouldSetDriverInactive
      }
    })
  }


  endRide(ride: RideDetails) {
    this.rideService.endRide(ride.id).subscribe((ride) => {
      this.notifications = this.notifications.filter(item => item.ride.id !== ride.id)
      this.updateRouteDisplay.emit();
    })
  }
}
