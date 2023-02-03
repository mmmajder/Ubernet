import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Store} from "@ngxs/store";
import {Customer} from "../../../model/User";
import {CurrentlyLogged, UpdateCustomerData} from "../../../store/actions/loggedUser.actions";
import {MatSnackBar} from "@angular/material/snack-bar";
import {EncodedImage, ImageService} from "../../../services/image.service";
import {NavbarComponent} from "../../sidenav/navbar/navbar.component";

@Component({
  selector: 'app-profile-data',
  templateUrl: './profile-data.component.html',
  styleUrls: ['./profile-data.component.css']
})
export class ProfileDataComponent implements OnInit {
  email = "";
  phoneNumber = "";
  name = "";
  lastName = "";
  city = "";
  role = "CUSTOMER";

  phoneFormControl = new FormControl('', [Validators.required]);
  nameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  cityFormControl = new FormControl('', [Validators.required]);

  hasSelectedFile = false;
  selectedImage: File;
  profileImageSrc: string;
  @ViewChild('fileUploader') fileUploader: ElementRef;

  constructor(private store: Store, private _snackBar: MatSnackBar, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe((resp) => {
      this.name = resp.loggedUser.name;
      this.lastName = resp.loggedUser.surname;
      this.phoneNumber = resp.loggedUser.phoneNumber;
      this.city = resp.loggedUser.city;
      this.email = resp.loggedUser.email;
      this.role = resp.loggedUser.role;
      this.getProfileImage();
    });
  }

  update() {
    if (this.role == "DRIVER")
      this.updateDriver();
    else
      this.updateCustomer();
  }

  updateCustomer() {
    const user = new Customer(this.name, this.lastName, this.email, this.phoneNumber, this.city);
    this.store.dispatch(new UpdateCustomerData(user)).subscribe({
      next: () => this._snackBar.open("Data updated successfully.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      }),
      error: () => this._snackBar.open("Error occurred while updating data.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    });
  }

  public selectFile(event: Event) {
    const input = (<HTMLInputElement>event.target)
    if (input.files !== null){
      const file:File = input.files[0];
      this.selectedImage = file;
      this.hasSelectedFile = true;
      this.newProfileImagePreview(file);
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

  public newProfileImagePreview(file : File){
    if (file !== null){
      const reader = new FileReader();
      reader.readAsDataURL(file);

      reader.onload = () => {
        this.profileImageSrc = reader.result as string;
      };

    }
  }

  public getProfileImage() {
    this.imageService.getProfileImage(this.email)
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

  private updateDriver() {
    // TODO
  }
}
