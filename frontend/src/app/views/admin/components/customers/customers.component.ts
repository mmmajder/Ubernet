import {Component, OnInit} from '@angular/core';
import {SimpleUser} from "../../../../model/User";
import {MatTableDataSource} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {Store} from "@ngxs/store";
import {EncodedImage, ImageService} from "../../../../services/image.service";
import {Customers} from "../../../../store/actions/customers.actions";
import {CustomersProfileDialogComponent} from "../customers-profile-dialog/customers-profile-dialog.component";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers: SimpleUser[];
  customersList: MatTableDataSource<SimpleUser> = new MatTableDataSource<SimpleUser>();
  displayedColumns: string[] = ['profilePicture', 'name', 'email'];
  profilePictures: Map<string, string> = new Map<string, string>();

  constructor(private customersProfile: MatDialog, private store: Store, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new Customers()).subscribe((resp) => {
      this.customers = resp.customers;
      this.customersList = new MatTableDataSource<SimpleUser>(this.customers);
      for (let i = 0; i < this.customers.length; i++) {
        this.imageService.getProfileImage(this.customers[i].email)
          .subscribe((encodedImage: EncodedImage) => {
            if (encodedImage === null)
              this.profilePictures.set(this.customers[i].email, "../../../../assets/default-profile-picture.jpg");
            else
              this.profilePictures.set(this.customers[i].email, `data:image/jpeg;base64,${encodedImage.data}`);
          });
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.customersList.filter = filterValue.trim().toLowerCase();
  }

  openCustomersProfileDialog(element: SimpleUser) {
    const dialogRef = this.customersProfile.open(CustomersProfileDialogComponent, {panelClass: 'no-padding-card'});
    dialogRef.componentInstance.userEmail = element.email;
  }

}
