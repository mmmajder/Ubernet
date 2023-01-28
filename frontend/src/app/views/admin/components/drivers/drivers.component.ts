import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DriverListItem} from "../../model/DriverListItem";
import {MatDialog} from "@angular/material/dialog";
import {
  RegisterNewDriverDialogComponent
} from "../register-new-driver-dialog/register-new-driver-dialog.component";
import {DriversProfileDialogComponent} from "../drivers-profile-dialog/drivers-profile-dialog.component";
import {Store} from "@ngxs/store";
import {Drivers} from "../../../../store/actions/drivers.actions";
import {Driver} from "../../../../model/Driver";
import {ImageService} from "../../../../services/image.service";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.css']
})
export class DriversComponent implements OnInit {

  displayedColumns: string[] = ['profilePicture', 'name', 'email'];
  driversList: MatTableDataSource<DriverListItem> = new MatTableDataSource<DriverListItem>();
  drivers: Driver[];
  profilePictures: Map<string, string> = new Map<string, string>();

  constructor(private driversProfile: MatDialog, private registerNewDriverDialog: MatDialog, private store: Store, private imageService: ImageService) {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.driversList.filter = filterValue.trim().toLowerCase();
  }

  ngOnInit(): void {
    this.store.dispatch(new Drivers()).subscribe((resp) => {
      console.log(resp);
      this.drivers = resp.drivers;
      this.driversList = new MatTableDataSource<DriverListItem>(this.usersToDriverListItems(this.drivers));
    });
  }

  registerNewDriver() {
    this.registerNewDriverDialog.open(RegisterNewDriverDialogComponent, {
      width: '600px',
      height: '600px'
    });
  }

  blockDriver(element: DriverListItem) {
    // TODO
  }

  openDriversProfileDialog(element: DriverListItem) {
    const dialogRef = this.driversProfile.open(DriversProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = element.email;
  }

  private usersToDriverListItems(users: Driver[]): DriverListItem[] {
    const driverList: DriverListItem[] = [];
    for (let i = 0; i < users.length; i++) {
      driverList.push(new DriverListItem(users[i].email, users[i].name + ' ' + users[i].surname));
      this.imageService.getProfileImage(users[i].email)
        .subscribe((encodedImage: any) => {
          if (encodedImage === null)
            this.profilePictures.set(users[i].email, "../../../../assets/taxi.jpg");
          else
            this.profilePictures.set(users[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
        });
    }
    return driverList;
  }
}
