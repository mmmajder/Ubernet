import {Component, Input, OnInit} from '@angular/core';
import * as L from "leaflet";
import {RideDetails} from "../../model/RideDetails";
import {RidesHistoryService} from "../../services/rides-history.service";
import {EncodedImage, ImageService} from "../../services/image.service";
import {SimpleUser} from "../../model/User";
import {RideReview} from "../../model/Review";
import {Place} from "../../model/Position";
import {FavoriteRoutesService} from "../../services/favorite-routes.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CurrentlyLogged} from "../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {
  DriversProfileDialogComponent
} from "../../views/admin/components/drivers-profile-dialog/drivers-profile-dialog.component";
import {
  CustomersProfileDialogComponent
} from "../../views/admin/components/customers-profile-dialog/customers-profile-dialog.component";

@Component({
  selector: 'app-ride-details-dialog',
  templateUrl: './ride-details-dialog.component.html',
  styleUrls: ['./ride-details-dialog.component.css']
})
export class RideDetailsDialogComponent implements OnInit {
  @Input() id: number;
  @Input() dialogRef: MatDialogRef<any>;
  public customerEmail = "";
  public isFavorite = false;
  public userRole = "CUSTOMER";

  private map: L.Map;
  ride: RideDetails = new RideDetails();
  profilePictures: Map<string, string> = new Map<string, string>();
  reviews: Map<string, RideReview> = new Map<string, RideReview>();
  showReviews: Map<string, boolean> = new Map<string, boolean>();

  constructor(private dialog: MatDialog, private rideService: RidesHistoryService, private router: Router, private store: Store, private _snackBar: MatSnackBar, private imageService: ImageService, private routeService: FavoriteRoutesService) {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.userRole = resp.loggedUser.role;
        if (resp.loggedUser.role == "CUSTOMER") {
          this.customerEmail = resp.loggedUser.email;
          this.isRouteFavorite(resp.loggedUser.email);
        }
      }
    });
  }

  ngOnInit(): void {
    this.rideService.getRideById(this.id).subscribe(data => {
      this.ride = data;
      this.loadProfilePictures(this.ride.customers);
      this.loadProfilePictures([this.ride.driver]);
      this.loadReviews(data);
      console.log("RIDE: ", this.ride);
      this.initMap();
    });
  }

  loadProfilePictures(customers: SimpleUser[]) {
    for (let i = 0; i < customers.length; i++) {
      this.imageService.getProfileImage(customers[i].email)
        .subscribe((encodedImage: EncodedImage) => {
          if (encodedImage === null)
            this.profilePictures.set(customers[i].email, "../../../../assets/default-profile-picture.jpg");
          else {
            this.profilePictures.set(customers[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
          }
        });
    }
  }

  openDriversProfileDialog() {
    const dialogRef = this.dialog.open(DriversProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = this.ride.driver.email;
    dialogRef.componentInstance.userRole = this.userRole;
    this.dialogRef.close();
  }

  openCustomersProfileDialog(email: string) {
    const dialogRef = this.dialog.open(CustomersProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = email;
    dialogRef.componentInstance.userRole = this.userRole;
    this.dialogRef.close();
  }

  orderRide() {
    this.router.navigate(["/map", this.ride.id]);
    this.dialogRef.close();
  }

  drawCheckpointsOnMap() {
    const waypoints: L.LatLng[] = [];
    console.log(this.ride)
    this.ride.checkPoints.forEach((place: Place) => {
      waypoints.push(L.latLng(place.position.y, place.position.x));
    });

    L.Routing.control({
      waypoints: waypoints,
      routeWhileDragging: false,
      addWaypoints: false,
    }).addTo(this.map);
  }

  loadReviews(ride: RideDetails) {
    for (const review of ride.reviews) {
      this.reviews.set(review.customer.email, review);
    }
    for (const customer of ride.customers) {
      this.showReviews.set(customer.email, false);
    }
  }

  hasReviews(email: string) {
    return this.reviews.has(email);
  }

  toggleReviews(email: string) {
    this.showReviews.set(email, !this.showReviews.get(email));
  }

  private initMap() {
    this.map = L.map('map').setView([45.267136, 19.833549], 11);
    const mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(this.map);
    this.drawCheckpointsOnMap();
  }

  addToFavorites() {
    this.routeService.addToFavoriteRoutes(this.customerEmail, this.id).subscribe({
      next: () => {
        this._snackBar.open("Successfully added to favorite routes!", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        });
        this.isFavorite = true;
      },
      error: () => this._snackBar.open("Something went wrong!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    })
  }

  removeFromFavorites() {
    this.routeService.removeFromFavoriteRoutes(this.customerEmail, this.id).subscribe({
      next: () => {
        this._snackBar.open("Successfully removed from favorite routes!", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        });
        this.isFavorite = false;
      },
      error: () => this._snackBar.open("Something went wrong!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    })
  }

  isRouteFavorite(customerEmail: string) {
    this.routeService.isRouteFavorite(customerEmail, this.id).subscribe({
      next: (value: boolean) => this.isFavorite = value,
      error: () => this._snackBar.open("Something went wrong!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    })
  }
}
