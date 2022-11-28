import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {tap} from "rxjs";
import {DriversService} from "../../services/drivers.service";
import {Drivers} from "../actions/drivers.actions";
import {Driver} from "../../model/Driver";

@State<Driver[]>({
  name: 'drivers',
  defaults: [] as Driver[]
})
@Injectable()
export class DriversState {

  @Selector()
  static drivers(state: Driver[]) {
    return state;
  }

  constructor(private driversService: DriversService) {
  }

  @Action(Drivers)
  getDrivers(ctx: StateContext<Driver[]>) {
    return this.driversService.getDrivers()
      .pipe(tap((drivers: Driver[]) =>
        ctx.setState(drivers)
      ));
  };

}
