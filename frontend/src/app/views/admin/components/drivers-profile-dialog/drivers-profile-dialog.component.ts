import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/User";
import {UserService} from "../../../../services/user.service";
import {ImageService} from "../../../../services/image.service";

@Component({
  selector: 'app-drivers-profile-dialog',
  templateUrl: './drivers-profile-dialog.component.html',
  styleUrls: ['./drivers-profile-dialog.component.css']
})
export class DriversProfileDialogComponent implements OnInit {
  @Input() userEmail: string;
  user: User = new User();
  profileImageSrc: string;

  constructor(private userService: UserService, private imageService: ImageService) {
  }

  ngOnInit() {
    this.userService.getUser(this.userEmail).subscribe(
      (user: User) => this.user = user
    );
    this.imageService.getProfileImage(this.userEmail)
      .subscribe((encodedImage: any) => {
        if (encodedImage === null)
          this.profileImageSrc = "../../../../assets/taxi.jpg";
        else
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }
}
