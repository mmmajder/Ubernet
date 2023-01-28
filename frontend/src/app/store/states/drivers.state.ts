import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {tap} from "rxjs";
import {DriversService} from "../../services/drivers.service";
import {Drivers} from "../actions/drivers.actions";
import {DriverDTO} from "../../model/DriverDTO";

@State<DriverDTO[]>({
  name: 'drivers',
  defaults: [] as DriverDTO[]
})
@Injectable()
export class DriversState {

  @Selector()
  static drivers(state: DriverDTO[]) {
    return state;
  }

  constructor(private driversService: DriversService) {
  }

  @Action(Drivers)
  getDrivers(ctx: StateContext<DriverDTO[]>) {
    return this.driversService.getDrivers()
      .pipe(tap((drivers: DriverDTO[]) =>
        ctx.setState(drivers)
      ));
  }

}
