import {Place} from "./Position";
import {SimpleUser} from "./User";
import {Review, RideReview} from "./Review";

export class RideDetails {
  id: number;
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
