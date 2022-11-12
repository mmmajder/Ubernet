import { Component, OnInit } from '@angular/core';
import {User} from "../../../../model/User";
import {Store} from "@ngxs/store";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";

@Component({
  selector: 'app-profile-customer-container',
  templateUrl: './profile-customer-container.component.html',
  styleUrls: ['./profile-customer-container.component.css']
})
export class ProfileCustomerContainerComponent implements OnInit {

  loggedUser: User;

  constructor(private store: Store) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe((resp) => {
      console.log(resp);
      console.log("ADFEKOGJASEO")
      this.loggedUser = resp;
    });
  }

}
