import {Injectable} from "@angular/core";
import {Action, Selector, State, StateContext} from "@ngxs/store";
import {tap} from "rxjs";
import {SimpleUser} from "../../model/User";
import {Customers} from "../actions/customers.actions";
import {CustomersService} from "../../services/customers.service";

@State<SimpleUser[]>({
  name: 'customers',
  defaults: [] as SimpleUser[]
})
@Injectable()
export class CustomersState {

  @Selector()
  static customers(state: SimpleUser[]) {
    return state;
  }

  constructor(private customersService: CustomersService) {
  }

  @Action(Customers)
  getCustomers(ctx: StateContext<SimpleUser[]>) {
    return this.customersService.getCustomers()
      .pipe(tap((customers: SimpleUser[]) =>
        ctx.setState(customers)
      ));
  };

}
