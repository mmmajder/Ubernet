import {Component, OnInit} from '@angular/core';
import {Store} from "@ngxs/store";
import {User} from "../../../../model/User";

@Component({
  selector: 'app-driver-container',
  templateUrl: './driver-container.component.html',
  styleUrls: ['./driver-container.component.css']
})
export class DriverContainerComponent implements OnInit {
  loggedUser: User;

  constructor(private store: Store) {
  }

  ngOnInit(): void {
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.loggedUser = user;
      }
    })
  }

}
