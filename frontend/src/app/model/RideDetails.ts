import {Place} from "./Position";
import {Driver, SimpleUser} from "./User";
import {RideReview} from "./Review";
import {RouteDTO} from "./RouteDTO";
import {Payment} from "./Payment";

export class RideDetails {
  id: number;
  route: RouteDTO;
  checkPoints: Place[];
  totalPrice: number;
  driver: Driver = new Driver();
  customers: SimpleUser[] = [];
  scheduledStart: string;
  actualStart: string;
  actualEnd: string;
  reservationTime: string;
  reviews: RideReview[];
  payment: Payment;
  canceled: boolean;
}
