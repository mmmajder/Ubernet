import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Store} from "@ngxs/store";
import {Customer} from "../../../model/User";
import {CurrentlyLogged, UpdateCustomerData} from "../../../store/actions/loggedUser.actions";

@Component({
  selector: 'app-profile-data',
  templateUrl: './profile-data.component.html',
  styleUrls: ['./profile-data.component.css']
})
export class ProfileDataComponent implements OnInit {
  email: string = "";
  phoneNumber: string = "";
  name: string = "";
  lastName: string = "";
  city: string = "";

  phoneFormControl = new FormControl('', [Validators.required]);
  nameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  cityFormControl = new FormControl('', [Validators.required]);

  constructor(private store: Store) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe((resp) => {
      this.name = resp.loggedUser.name;
      this.lastName = resp.loggedUser.surname;
      this.phoneNumber = resp.loggedUser.phoneNumber;
      this.city = resp.loggedUser.city;
      this.email = resp.loggedUser.email;
    });
  }

  update() {
    let user = new Customer(this.name, this.lastName, this.email, this.phoneNumber, this.city);
    console.log(user)
    this.store.dispatch(new UpdateCustomerData(user)).subscribe(() => console.log("saieyge"))
  }
}
