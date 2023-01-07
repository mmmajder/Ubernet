import {Component, Input, OnInit} from '@angular/core';
import * as L from "leaflet";
import {RideDetails} from "../../model/RideDetails";
import {RidesHistoryService} from "../../services/rides-history.service";
import {ImageService} from "../../services/image.service";
import {SimpleUser} from "../../model/User";

@Component({
  selector: 'app-ride-details-dialog',
  templateUrl: './ride-details-dialog.component.html',
  styleUrls: ['./ride-details-dialog.component.css']
})
export class RideDetailsDialogComponent implements OnInit {
  private map: L.Map;
  ride: RideDetails = new RideDetails();
  @Input() id: number;
  profilePictures: Map<string, string> = new Map<string, string>();

  constructor(private rideService: RidesHistoryService, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.rideService.getRideById(this.id).subscribe(data => {
      this.ride = data;
      this.loadProfilePictures(this.ride.customers);
      console.log("RIDE: ", this.ride)
    });

    this.map = L.map('map').setView([45.267136, 19.833549], 11);
    let mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(this.map);
  }

  loadProfilePictures(customers: SimpleUser[]) {
    customers.push(this.ride.driver);
    for (let i = 0; i < customers.length; i++) {
      this.imageService.getProfileImage(customers[i].email)
        .subscribe((encodedImage: any) => {
          if (encodedImage === null)
            this.profilePictures.set(customers[i].email, "../../../../assets/taxi.jpg");
          else {
            console.log("IMA SLIKU ALI NIJE SETOVANA")
            this.profilePictures.set(customers[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
          }
        });
    }
  }
}
