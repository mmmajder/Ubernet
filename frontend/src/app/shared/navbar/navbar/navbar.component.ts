import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NavbarStore} from "../../stores/navbar.store";
import {NotificationDto} from "../../../services/navbar.service";
import {RestaurantsStore} from "../../stores/restaurants.store";
import {SocketService} from "../../../services/sockets.service";
import {RestaurantStore} from "../../stores/restaurant.store";

export interface MyNotification {
  colorBell: boolean;
  message: string;
}

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {

  notifications: NotificationDto[] = [];
  colorBell: boolean = false;

  constructor(private router: Router, private navbarStore: NavbarStore, private restaurantsStore: RestaurantsStore, private restaurantStore: RestaurantStore, private socketService: SocketService) {

  }

  ngOnInit(): void {
    this.socketService.createWebSocket(this);

    this.navbarStore.notifications$.subscribe(value => {
      this.notifications = value;
    });
    this.navbarStore.colorBell$.subscribe(value => {
      this.colorBell = value;
    });
    this.navbarStore.getNotifications();

  }

  goToRestaurants() {
    this.router.navigate(['']);
  }

  handleMessage(message: any) {
    console.log("HANDLE MESSAGE: ", message.body)
    if (this.router.url === "/") {
      this.restaurantsStore.getRestaurants();
      this.navbarStore.createNotification({message: message.body, colorBell: false});
    } else {
      this.restaurantStore.getRestaurant(this.router.url.split('restaurant/')[1]);
      this.navbarStore.createNotification({message: message.body, colorBell: true});
    }
  }

  menuOpened() {
    this.navbarStore.openedNotification();
  }
}
