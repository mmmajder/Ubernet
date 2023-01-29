import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/User";
import {UserService} from "../../../../services/user.service";
import {ImageService} from "../../../../services/image.service";

@Component({
  selector: 'app-customers-profile-dialog',
  templateUrl: './customers-profile-dialog.component.html',
  styleUrls: ['./customers-profile-dialog.component.css']
})
export class CustomersProfileDialogComponent implements OnInit {

  @Input() userEmail: string;
  @Input() userRole: string = "";
  user: User = new User();
  profileImageSrc: string;

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
