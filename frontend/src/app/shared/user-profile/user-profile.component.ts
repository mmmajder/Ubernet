import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {ImageService} from "../../services/image.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  @Input() userEmail: string;
  profileImageSrc: string;
  user: User = new User();

  constructor(private userService: UserService, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.userService.getUser(this.userEmail).subscribe(
      (user: User) => this.user = user
    );
    this.imageService.getProfileImage(this.userEmail)
      .subscribe((encodedImage: any) => {
        if (encodedImage === null)
          this.profileImageSrc = "../../../../assets/default-profile-picture.jpg";
        else
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }
}
