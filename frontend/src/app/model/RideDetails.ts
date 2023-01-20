import {Place} from "./Position";
import {SimpleUser} from "./User";
import {RideReview} from "./Review";
import {RouteDTO} from "./RouteDTO";

export class RideDetails {
  id: number;
  route: RouteDTO;
  checkPoints: Place[];
  totalPrice: number;
  driver: SimpleUser = new SimpleUser();
  customers: SimpleUser[] = [];
  scheduledStart: string;
  actualStart: string;
  actualEnd: string;
  reservationTime: string;
  carReviews: RideReview[];
  driverReviews: RideReview[];
}
