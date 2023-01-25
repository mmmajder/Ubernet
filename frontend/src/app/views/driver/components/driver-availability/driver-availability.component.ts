import {Component, Input, OnInit} from '@angular/core';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {User} from "../../../../model/User";
import {secondsToHm} from "../../../../services/utils.service";
import {DriversService} from "../../../../services/drivers.service";

@Component({
  selector: 'app-driver-availability',
  templateUrl: './driver-availability.component.html',
  styleUrls: ['./driver-availability.component.css']
})
export class DriverAvailabilityComponent implements OnInit {
  @Input() loggedUser: User
  workingHours: string = "0 minutes";
  isActive: boolean;
  private stompClient: any;

  constructor(private driverService: DriversService) {
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection()
    this.driverService.getDriver(this.loggedUser.email).subscribe((driver) => {
      console.log(driver)
      this.isActive = driver.isWorking
    })
    this.driverService.getNumberOfActiveHoursInLast24h(this.loggedUser.email).subscribe((seconds: number) => {
      console.log(seconds)
      this.workingHours = secondsToHm(seconds)
    })
  }

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openActivitySocket();
    });
  }

  openActivitySocket() {
    this.stompClient.subscribe("/driver/active-seconds-" + this.loggedUser.email, (message: any) => {
      let seconds: number = JSON.parse(message.body)
      this.workingHours = secondsToHm(seconds);
    })
  }

  toggleActivity() {
    this.driverService.toggleActivity(this.loggedUser.email).subscribe(() => {
    })
  }
}
