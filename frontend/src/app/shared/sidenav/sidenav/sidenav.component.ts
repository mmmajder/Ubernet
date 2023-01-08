import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";
import {Logout} from "../../../store/actions/authentication.actions";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {User} from "../../../model/User";
import {CustomersService} from "../../../services/customers.service";
import {ImageService} from "../../../services/image.service";

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
  numberOfTokens: number = 0;

  public profilePictureSrc: string;
  private hasRequestedProfilePicture: boolean = false;
  public static _this: any;

  someMethod() {
    this.trigger?.openMenu();
  }

  constructor(private store: Store, private router: Router, private customerService: CustomersService, private imageService: ImageService) {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.user = user;
        this.setNumberOfTokens();

      }
    })
    SidenavComponent._this = this;
  }

  ngOnInit(): void {

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
        next: value => this.numberOfTokens = value
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
}
