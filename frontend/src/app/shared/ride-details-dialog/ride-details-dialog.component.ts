import {Component, Input, OnInit} from '@angular/core';
import * as L from "leaflet";
import {RideDetails} from "../../model/RideDetails";
import {RidesHistoryService} from "../../services/rides-history.service";
import {ImageService} from "../../services/image.service";
import {SimpleUser} from "../../model/User";
import {RideReview} from "../../model/Review";
import {PositionInTime} from "../../model/PositionInTime";
import {Place} from "../../model/Position";

@Component({
  selector: 'app-ride-details-dialog',
  templateUrl: './ride-details-dialog.component.html',
  styleUrls: ['./ride-details-dialog.component.css']
})
export class RideDetailsDialogComponent implements OnInit {
  @Input() id: number;

  private map: L.Map;
  ride: RideDetails = new RideDetails();
  profilePictures: Map<string, string> = new Map<string, string>();
  driverReviews: Map<string, RideReview> = new Map<string, RideReview>();
  carReviews: Map<string, RideReview> = new Map<string, RideReview>();
  showReviews: Map<string, boolean> = new Map<string, boolean>();

  constructor(private rideService: RidesHistoryService, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.rideService.getRideById(this.id).subscribe(data => {
      this.ride = data;
      this.loadProfilePictures(this.ride.customers);
      this.loadProfilePictures([this.ride.driver]);
      this.loadReviews(data);
      console.log("RIDE: ", this.ride);
      this.initMap()
    });
  }

  loadProfilePictures(customers: SimpleUser[]) {
    for (let i = 0; i < customers.length; i++) {
      this.imageService.getProfileImage(customers[i].email)
        .subscribe((encodedImage: any) => {
          if (encodedImage === null)
            this.profilePictures.set(customers[i].email, "../../../../assets/taxi.jpg");
          else {
            this.profilePictures.set(customers[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
          }
        });
    }
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
    }).on('routesfound', () => {
    }).addTo(this.map);
  }

  loadReviews(ride: RideDetails) {
    for (const review of ride.driverReviews) {
      console.log(review);
      this.driverReviews.set(review.customer.email, review);
    }
    for (const review of ride.carReviews) {
      this.carReviews.set(review.customer.email, review);
    }
    for (const customer of ride.customers) {
      this.showReviews.set(customer.email, false);
    }
  }

  hasReviews(email: string) {
    return this.driverReviews.has(email) || this.carReviews.has(email);
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
}
