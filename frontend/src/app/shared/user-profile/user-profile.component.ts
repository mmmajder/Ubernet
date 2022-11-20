import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {ImageService} from "../../services/image.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @Input() user: User;
  profileImageSrc: string;

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.getProfilePicture();
  }

  getProfilePicture() {
    this.imageService.getProfileImage(this.user.email)
      .subscribe((encodedImage: any) => {
        if (encodedImage === null)
          this.profileImageSrc = "../../../../assets/taxi.jpg";
        else
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }
}
