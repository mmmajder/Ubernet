import {Component, OnInit} from '@angular/core';
import {Store} from "@ngxs/store";
import {secondsToHm} from "../../services/utils.service";
import {DriversService} from "../../services/drivers.service";
import {CurrentlyLogged} from "../../store/actions/loggedUser.actions";

@Component({
  selector: 'app-active-time-driver',
  templateUrl: './active-time-driver.component.html',
  styleUrls: ['./active-time-driver.component.css']
})
export class ActiveTimeDriverComponent implements OnInit {
  workingHours = "0 minutes"

  constructor(private store: Store, private driverService: DriversService) {
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe((resp) => {
      if (resp.loggedUser.role === "DRIVER")
        this.driverService.getNumberOfActiveHoursInLast24h(resp.loggedUser.email).subscribe((seconds: number) => {
          this.workingHours = secondsToHm(seconds)
        })
    });
  }

}
