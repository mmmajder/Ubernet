import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DriverListItem} from "../../model/DriverListItem";
import {MatDialog} from "@angular/material/dialog";
import {
  RegisterNewDriverDialogComponent
} from "../../components/register-new-driver-dialog/register-new-driver-dialog.component";
import {DriversProfileDialogComponent} from "../../components/drivers-profile-dialog/drivers-profile-dialog.component";
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

  displayedColumns: string[] = ['activity', 'profilePicture', 'name', 'block'];
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
    // this.driversList = this.driverService.getDrivers().subscribe();
    // this.drivers = this.store.selectSnapshot(DriversState.drivers);

    this.store.dispatch(new Drivers()).subscribe((resp) => {
      console.log(resp);
      this.drivers = resp.drivers;
      this.driversList = new MatTableDataSource<DriverListItem>(this.usersToDriverListItems(this.drivers));
    });
  }

  registerNewDriver() {
    let dialogRef = this.registerNewDriverDialog.open(RegisterNewDriverDialogComponent);
  }

  blockDriver(element: DriverListItem) {
    // TODO
  }

  openDriversProfileDialog(element: DriverListItem) {
    let dialogRef = this.driversProfile.open(DriversProfileDialogComponent);
    dialogRef.componentInstance.userEmail = element.email;
    console.log("DRIVERS.COMPONENT")
    console.log(element.email)
  }

  private usersToDriverListItems(users: Driver[]): DriverListItem[] {
    let driverList: DriverListItem[] = [];
    for (let i = 0; i < users.length; i++) {
      driverList.push(new DriverListItem(users[i].email, users[i].name + ' ' + users[i].surname, users[i].isWorking));
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
