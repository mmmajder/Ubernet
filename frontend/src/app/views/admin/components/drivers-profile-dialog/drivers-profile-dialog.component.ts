import {Component, Input, OnInit} from '@angular/core';
import {User} from "../../../../model/User";
import {UserService} from "../../../../services/user.service";
import {EncodedImage, ImageService} from "../../../../services/image.service";
import {CarService} from "../../../../services/car.service";
import {Car} from "../../../../model/Car";

@Component({
  selector: 'app-drivers-profile-dialog',
  templateUrl: './drivers-profile-dialog.component.html',
  styleUrls: ['./drivers-profile-dialog.component.css']
})
export class DriversProfileDialogComponent implements OnInit {
  @Input() userEmail = "";
  @Input() userRole = "";
  user: User = new User();
  profileImageSrc: string;
  car: Car;

  constructor(private userService: UserService, private carService: CarService, private imageService: ImageService) {
  }

  ngOnInit() {
    this.carService.getCar(this.userEmail).subscribe(
      (car: Car) => this.car = car
    );
    this.userService.getUser(this.userEmail).subscribe(
      (user: User) => this.user = user
    );
    this.imageService.getProfileImage(this.userEmail)
      .subscribe((encodedImage: EncodedImage) => {
        if (encodedImage === null)
          this.profileImageSrc = "../../../../assets/default-profile-picture.jpg";
        else
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }
}
