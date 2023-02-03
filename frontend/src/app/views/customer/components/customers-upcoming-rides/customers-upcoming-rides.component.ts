import {Component, OnInit} from '@angular/core';
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {Customer, User} from "../../../../model/User";
import {Router} from "@angular/router";
import {NotificationDTO} from "../../../../model/NotificationDTO";
import {RideService} from "../../../../services/ride.service";
import {RideDTO} from "../../../../model/RideDTO";
import {Message} from "stompjs";

@Component({
  selector: 'app-customers-upcoming-rides',
  templateUrl: './customers-upcoming-rides.component.html',
  styleUrls: ['./customers-upcoming-rides.component.css']
})
export class CustomersUpcomingRidesComponent implements OnInit {
  loggedUser: User;
  timeLeft: number;
  arriveTime: string;
  start: string;
  destination: string;
  friends: string[];
  display: boolean;
  photo: string;
  private stompClient: any;

  constructor(private store: Store, private router: Router, private rideService: RideService) {
    this.display = false;
    this.photo = '../../assets/default-profile-picture.jpg';
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.loggedUser = resp.loggedUser;
        this.initializeWebSocketConnection();
      },
      error: () => this.router.navigate(['/'])
    });
  }

  initializeWebSocketConnection() {
    const ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openDashboardSocket();
    });
  }

  private openDashboardSocket() {
    this.stompClient.subscribe("/customer/time-until-ride-" + this.loggedUser.email, (message: Message) => {
      this.display = true
      const notification: NotificationDTO = JSON.parse(message.body)
      this.timeLeft = Math.floor(+notification.text / 60)
      const arriveTime = new Date()
      arriveTime.setMinutes(arriveTime.getMinutes() + this.timeLeft);
      this.arriveTime = arriveTime.getHours() + ":" + arriveTime.getMinutes().toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      });
      this.rideService.getById(notification.rideId).subscribe((ride: RideDTO) => {
        this.start = ride.route.checkPoints[0].name
        const numberOfCheckpoints = ride.route.checkPoints.length
        this.destination = ride.route.checkPoints[numberOfCheckpoints - 1].name
        this.friends = ride.customers.map((customer: Customer) => {
          return customer.name + " " + customer.surname
        })
      })
    })

  }
}
