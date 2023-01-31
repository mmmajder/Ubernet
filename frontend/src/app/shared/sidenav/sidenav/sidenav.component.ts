import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {Logout} from "../../../store/actions/authentication.actions";
import {Select, Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {User} from "../../../model/User";
import {CustomersService} from "../../../services/customers.service";
import {ImageService} from "../../../services/image.service";
import {PaymentComponent} from "../../../views/customer/components/payment/payment.component";
import {MatDialog} from "@angular/material/dialog";
import {Observable, Subscription} from "rxjs";
import {TokensState} from "../../../store/states/tokens.state";
import {SetTokens} from "../../../store/actions/tokens.action";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {NotificationService} from "../../../services/notification.service";
import {MapComponent} from "../../../views/map/container/map/map.component";
import {NotificationDTO} from "../../../model/NotificationDTO";
import {DriversService} from "../../../services/drivers.service";
import {secondsToHm} from "../../../services/utils.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Client, Message} from "stompjs";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;
  @ViewChild(MapComponent) mapComponent: MapComponent;
  @Input() currentPage = 'dashboard';
  @Select(TokensState.value) numberOfTokens$!: Observable<number>;

  notificationsCustomer: NotificationDTO[];

  user: User;
  isActive = false;
  numberOfTokens!: number;
  private valueSubscription: Subscription;
  public profilePictureSrc: string;
  private hasRequestedProfilePicture = false;
  public static _this: any;
  notificationBadgeHidden: boolean;
  private stompClient: Client;
  driverActive: boolean;
  workingHours = "0 minutes";

  constructor(private _snackBar: MatSnackBar, private driverService: DriversService, public dialog: MatDialog, private store: Store, private router: Router, private customerService: CustomersService, private imageService: ImageService, private notificationService: NotificationService) {
    this.valueSubscription = this.numberOfTokens$.subscribe((value: number) => {
      this.numberOfTokens = value;
    });
    SidenavComponent._this = this;
  }

  ngOnInit(): void {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.user = user;
        this.setNumberOfTokens();
        this.notificationService.areNotificationSeen(this.user.email).subscribe((res: boolean) => {
          this.notificationBadgeHidden = res;
        })
        this.initializeWebSocketConnection();
        console.log(this.user)
        if (this.user.role === "DRIVER") {
          this.driverService.getDriver(this.user.email).subscribe((driver) => {
            this.driverActive = driver.isWorking
          })
          this.driverService.getNumberOfActiveHoursInLast24h(this.user.email).subscribe((seconds: number) => {
            console.log(seconds)
            this.workingHours = secondsToHm(seconds)
          })
        }
        if (this.user !== undefined && this.user !== null) {
          // this.hasRequestedProfilePicture = true;
          this.getProfilePicture();
        }
      }
    })
  }

  initializeWebSocketConnection() {
    const ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    // this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/notify/split-fare-" + this.user.email, () => {
      this.updateNotificationBadge()
    })
    this.stompClient.subscribe("/customer/payback-" + this.user.email, (message: Message) => {
      const money: number = JSON.parse(message.body)
      this.store.dispatch([new SetTokens(money)])
    })
    this.stompClient.subscribe("/customer/everyone-payed-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/technical-problem-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/reservation-reminder-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/did-not-appear-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/car-start-point-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/car-start-point-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/customer/driver-inconsistency-" + this.user.email, () => {
      this.updateNotificationBadge();
    })
    this.stompClient.subscribe("/driver/active-seconds-" + this.user.email, (message: Message) => {
      const seconds: number = JSON.parse(message.body)
      this.workingHours = secondsToHm(seconds);
    })
  }

  // ngDoCheck(): void {
  //   if (this.user !== undefined && this.profilePictureSrc === undefined && !this.hasRequestedProfilePicture) {
  //     this.hasRequestedProfilePicture = true;
  //     this.getProfilePicture();
  //   }
  // }

  setNumberOfTokens() {
    if (this.user.role == "CUSTOMER") {
      this.customerService.getNumberOfTokens(this.user.email).subscribe({
        next: value => {
          this.store.dispatch([new SetTokens(value)])
        }
      })
    }
  }

  private getProfilePicture(): void {
    this.imageService.getProfileImage(this.user.email)
      .subscribe((encodedImage: any) => {
        console.log(encodedImage);
        if (encodedImage === null)
          this.profilePictureSrc = "assets/default-profile-picture.jpg";
        else
          this.profilePictureSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }

  public static changeProfilePicture(profilePictureSrc: string): void {
    SidenavComponent._this.profilePictureSrc = profilePictureSrc;
  }

  toggle() {
    this.isActive = !this.isActive;
  }

  logout() {
    this.store.dispatch(new Logout()).subscribe({});
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }

  addTokens() {
    this.dialog.open(PaymentComponent, {
      data: {
        "user": this.user
      }
    });
  }

  hideBadge() {
    this.notificationService.openNotificationForCustomer(this.user.email).subscribe((res: NotificationDTO[]) => {
      this.notificationBadgeHidden = true
      this.notificationsCustomer = res.reverse();
    })
  }

  updateNotificationBadge() {
    this.notificationBadgeHidden = false;
  }

  toggleDriverActivity() {
    this.driverService.toggleActivity(this.user.email, this.driverActive).subscribe({
      next: () => {
      },
      error: (resp) => {
        console.log(resp)
        this.driverActive = !this.driverActive
        this._snackBar.open(resp.error, '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      }
    })
  }
}
