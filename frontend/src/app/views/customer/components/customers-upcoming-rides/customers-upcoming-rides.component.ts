import {Component, OnInit} from '@angular/core';
import {ReservedRideItem} from "../../../../model/ReservedRideItem";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {Customer, User} from "../../../../model/User";
import {Router} from "@angular/router";
import {NotificationDTO} from "../../../../model/NotificationDTO";
import {RideService} from "../../../../services/ride.service";
import {RideDTO} from "../../../../model/RideDTO";

@Component({
  selector: 'app-customers-upcoming-rides',
  templateUrl: './customers-upcoming-rides.component.html',
  styleUrls: ['./customers-upcoming-rides.component.css']
})
export class CustomersUpcomingRidesComponent implements OnInit {
  rides: ReservedRideItem[] = [{
    'time': '15:10h',
    'timeLeft': '15min',
    'start': 'Bulevar Kralja Petra 5',
    'destination': 'Trzni centar Promenada',
    'friends': [
      {
        'name': 'Pera',
        'photo': '../../assets/default-profile-picture.jpg'
      },
      {
        'name': 'Pera',
        'photo': '../../assets/default-profile-picture.jpg'
      }]
  }];
  loggedUser: User
  timeLeft: number
  arriveTime: string;
  start: string
  destination: string
  friends: string[]
  display: boolean
  photo: string

  constructor(private store: Store, private router: Router, private rideService: RideService) {
    this.display = false
    this.photo = '../../assets/default-profile-picture.jpg'
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.loggedUser = resp.loggedUser;
        this.initializeWebSocketConnection()
      },
      error: () => this.router.navigate(['/'])
    });

  }

  private stompClient: any;

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openDashboardSocket();
    });
  }

  private openDashboardSocket() {
    this.stompClient.subscribe("/customer/time-until-ride-" + this.loggedUser.email, (message: any) => {
      this.display = true
      let notification: NotificationDTO = JSON.parse(message.body)
      this.timeLeft = Math.floor(+notification.text / 60)
      let arriveTime = new Date()
      arriveTime.setMinutes(arriveTime.getMinutes() + this.timeLeft);
      this.arriveTime = arriveTime.getHours() + ":" + arriveTime.getMinutes().toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      });
      this.rideService.getById(notification.rideId).subscribe((ride: RideDTO) => {
        this.start = ride.route.checkPoints[0].name
        let numberOfCheckpoints = ride.route.checkPoints.length
        this.destination = ride.route.checkPoints[numberOfCheckpoints - 1].name
        this.friends = ride.customers.map((customer: Customer) => {
          return customer.name + " " + customer.surname
        })
      })
    })

  }
}
