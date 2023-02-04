import {Customer} from "./User";
import {RideDetails} from "./RideDetails";

export class DriverInconsistency {
  id: number;
  ride: RideDetails;
  customers: Customer[];
}
