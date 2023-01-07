import {Place} from "./Position";
import {SimpleUser} from "./User";
import {Review} from "./Review";

export class RideDetails {
  id: number;
  checkPoints: Place[];
  totalPrice: number;
  driver: SimpleUser;
  customers: SimpleUser[];
  scheduledStart: string;
  actualStart: string;
  actualEnd: string;
  reservationTime: string;
  carReviews: Review[];
  driverReviews: Review[];
}
