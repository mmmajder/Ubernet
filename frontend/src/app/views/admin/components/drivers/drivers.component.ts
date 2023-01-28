import {Component, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DriverListItem} from "../../model/DriverListItem";
import {MatDialog} from "@angular/material/dialog";
import {
  RegisterNewDriverDialogComponent
} from "../register-new-driver-dialog/register-new-driver-dialog.component";
import {DriversProfileDialogComponent} from "../drivers-profile-dialog/drivers-profile-dialog.component";
import {DriverDTO} from "../../../../model/DriverDTO";
import {ImageService} from "../../../../services/image.service";
import {DriversService} from "../../../../services/drivers.service";
import {ChangesRequestDialogComponent} from "../changes-request-dialog/changes-request-dialog.component";

@Component({
  selector: 'app-drivers',
  templateUrl: './drivers.component.html',
  styleUrls: ['./drivers.component.css']
})
export class DriversComponent implements OnInit {

  displayedColumns: string[] = ['profilePicture', 'name', 'email', 'requestedChanges'];
  driversList: MatTableDataSource<DriverListItem> = new MatTableDataSource<DriverListItem>();
  drivers: DriverDTO[];
  profilePictures: Map<string, string> = new Map<string, string>();
  filterDriversByRequests: boolean = false;

  constructor(private driversService: DriversService, private dialog: MatDialog, private registerNewDriverDialog: MatDialog, private imageService: ImageService) {
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.driversList.filter = filterValue.trim().toLowerCase();
  }

  ngOnInit(): void {
    this.getDrivers(false);
  }

  public getDrivers(filterByRequests: boolean) {
    this.filterDriversByRequests = filterByRequests;
    this.driversService.getDrivers().subscribe((drivers) => {
      this.drivers = drivers;
      console.log(this.drivers);
      this.driversList = new MatTableDataSource<DriverListItem>(this.usersToDriverListItems(this.drivers));
    });
  }

  registerNewDriver() {
    this.registerNewDriverDialog.open(RegisterNewDriverDialogComponent, {
      width: '600px',
      height: '600px'
    });
  }

  openChangesRequest(element: DriverListItem) {
    const dialogRef = this.dialog.open(ChangesRequestDialogComponent, {
      width: '600px',
      height: '600px'
    })
    dialogRef.componentInstance.driverEmail = element.email;
  }

  blockDriver(element: DriverListItem) {
    // TODO
  }

  openDriversProfileDialog(element: DriverListItem) {
    const dialogRef = this.dialog.open(DriversProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = element.email;
  }

  private usersToDriverListItems(users: DriverDTO[]): DriverListItem[] {
    const driverList: DriverListItem[] = [];
    for (let i = 0; i < users.length; i++) {
      if (!this.filterDriversByRequests || (this.filterDriversByRequests && users[i].requestedChanges)) {
        driverList.push(new DriverListItem(users[i].email, users[i].name + ' ' + users[i].surname, users[i].requestedChanges));
        this.imageService.getProfileImage(users[i].email)
          .subscribe((encodedImage: any) => {
            if (encodedImage === null)
              this.profilePictures.set(users[i].email, "../../../../assets/taxi.jpg");
            else
              this.profilePictures.set(users[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
          });
      }
    }
    return driverList;
  }
}
