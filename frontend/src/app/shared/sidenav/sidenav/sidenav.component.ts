import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {Logout} from "../../../store/actions/authentication.actions";
import {Select, Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {Customer, User} from "../../../model/User";
import {CustomersService} from "../../../services/customers.service";
import {ImageService} from "../../../services/image.service";
import {PaymentComponent} from "../../../views/customer/components/payment/payment.component";
import {MatDialog} from "@angular/material/dialog";
import {Observable, Subscription} from "rxjs";
import {TokensState} from "../../../store/states/tokens.state";
import {SetTokens} from "../../../store/actions/tokens.action";
import {NotificationDTO} from "../../../model/NotificationDTO";
import * as SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {

  isActive: boolean = false;
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;

  user: User;
  @Input() currentPage: string = 'dashboard';

  @Input()

  @Select(TokensState.value) numberOfTokens$!: Observable<number>
  numberOfTokens!: number;
  private valueSubscription: Subscription;

  public profilePictureSrc: string;
  private hasRequestedProfilePicture: boolean = false;
  public static _this: any;
  notificationBadgeHidden: boolean;


  constructor(public dialog: MatDialog, private store: Store, private router: Router, private customerService: CustomersService, private imageService: ImageService, private notificationService: NotificationService) {
    this.valueSubscription = this.numberOfTokens$.subscribe((value: number) => {
      this.numberOfTokens = value;
    });

    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.user = user;
        this.setNumberOfTokens();
        this.notificationService.areNotificationSeen(this.user.email).subscribe((res: boolean) => {
          this.notificationBadgeHidden = res;
        })
      }
    })
    SidenavComponent._this = this;
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
  }

  private stompClient: any;

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/notify/split-fare-" + this.user.email, (message: any) => {
      console.log(message)
      console.log("Stigao sam na drugu stranu")
      this.notify(JSON.parse(message.body))
    })
  }


  ngDoCheck(): void {
    if (this.user !== undefined && this.profilePictureSrc === undefined && !this.hasRequestedProfilePicture) {
      this.hasRequestedProfilePicture = true;
      this.getProfilePicture();
    }
  }

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
          this.profilePictureSrc = "assets/taxi.jpg";
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
    this.store.dispatch(new Logout()).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => alert("Error occurred")
    });
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }

  addTokens() {
    const dialogRef = this.dialog.open(PaymentComponent, {
      data: {
        "user": this.user
      }
    });
  }

  hideBadge() {
    this.notificationService.openNotificationForCustomer(this.user.email).subscribe(() => {
      this.notificationBadgeHidden = true
    })
  }

  notify(notification: NotificationDTO) {
    console.log("notificationBadgeHidden")
    console.log(notification)
    console.log(this.user.email)
    // if (this.user.email===customer.email) {
    this.notificationBadgeHidden = false
    // }
  }

}
