import {Component, OnInit} from '@angular/core';
import {ImageService} from "../../../../services/image.service";
import {SimpleUser} from "../../../../model/User";
import {ChangesRequestDTO} from "../../../../model/ChangesRequest";

@Component({
  selector: 'app-changes-requests',
  templateUrl: './changes-requests.component.html',
  styleUrls: ['./changes-requests.component.css']
})
export class ChangesRequestsComponent implements OnInit {
  changesRequests: ChangesRequestDTO[];
  profilePictures: Map<string, string> = new Map<string, string>();

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
  }

  private loadProfileImages() {
    for (let i = 0; i < this.changesRequests.length; i++) {
      this.imageService.getProfileImage(this.changesRequests[i].driver.email)
        .subscribe((encodedImage: any) => {
          if (encodedImage === null)
            this.profilePictures.set(this.changesRequests[i].driver.email, "../../../../assets/taxi.jpg");
          else
            this.profilePictures.set(this.changesRequests[i].driver.email, `data:image/jpeg;base64,${encodedImage.data}`);
        });
    }
  }

}
