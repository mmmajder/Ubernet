import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngxs/store";
import {CurrentlyLogged} from "../../../store/actions/loggedUser.actions";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ImageService} from "../../../services/image.service";
import {SidenavComponent} from "../../sidenav/sidenav/sidenav.component";

@Component({
  selector: 'app-profile-picture',
  templateUrl: './profile-picture.component.html',
  styleUrls: ['./profile-picture.component.css']
})
export class ProfilePictureComponent implements OnInit {
  email = "";
  hasSelectedFile = false;
  selectedImage: any = null;
  profileImageSrc: string;
  @ViewChild('fileUploader') fileUploader: ElementRef;

  constructor(private store: Store, private _snackBar: MatSnackBar, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe((resp) => {
      this.email = resp.loggedUser.email;
      this.getProfileImage(resp.loggedUser.email);
    });
  }

  public selectFile(event: any) {
    this.selectedImage = event.target.files[0];
    this.hasSelectedFile = true;
  }

  public uploadProfileImage() {
    if (this.selectedImage != null) {
      this.imageService.postProfileImage(this.email, this.selectedImage)
        .subscribe((encodedImage: any) => {
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
          this.resetFileUploader();
          SidenavComponent.changeProfilePicture(`data:image/jpeg;base64,${encodedImage.data}`);
        });
    }
  }

  public getProfileImage(email: string) {
    this.imageService.getProfileImage(email)
      .subscribe((encodedImage: any) => {
        if (encodedImage === null)
          this.profileImageSrc = "../../../../assets/default-profile-picture.jpg";
        else
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }

  resetFileUploader() {
    this.fileUploader.nativeElement.value = null;
    this.hasSelectedFile = false;
  }
}
