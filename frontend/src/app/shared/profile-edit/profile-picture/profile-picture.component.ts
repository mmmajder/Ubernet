import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Store} from "@ngxs/store";
import {CurrentlyLogged} from "../../../store/actions/loggedUser.actions";
import {MatSnackBar} from "@angular/material/snack-bar";
import {EncodedImage, ImageService} from "../../../services/image.service";
import {NavbarComponent} from "../../sidenav/navbar/navbar.component";

@Component({
  selector: 'app-profile-picture',
  templateUrl: './profile-picture.component.html',
  styleUrls: ['./profile-picture.component.css']
})
export class ProfilePictureComponent implements OnInit {
  email = "";
  hasSelectedFile = false;
  selectedImage: File;
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

  public selectFile(event: Event) {
    const input = (<HTMLInputElement>event.target)
    if (input.files !== null) {
      const file: File = input.files[0];
      this.selectedImage = file;
      this.hasSelectedFile = true;
      this.newProfileImagePreview(file);
    }
  }

  public newProfileImagePreview(file: File) {
    if (file !== null) {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.profileImageSrc = reader.result as string;
      };
    }
  }

  public uploadProfileImage() {
    if (this.selectedImage != null) {
      this.imageService.postProfileImage(this.email, this.selectedImage)
        .subscribe((encodedImage: EncodedImage) => {
          this.profileImageSrc = `data:image/jpeg;base64,${encodedImage.data}`;
          this.resetFileUploader();
          NavbarComponent.changeProfilePicture(`data:image/jpeg;base64,${encodedImage.data}`);
        });
    }
  }

  public getProfileImage(email: string) {
    this.imageService.getProfileImage(email)
      .subscribe((encodedImage: EncodedImage) => {
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
