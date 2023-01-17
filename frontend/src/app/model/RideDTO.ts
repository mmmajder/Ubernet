import {PaymentDTO} from "./PaymentDTO";
import {Customer, UserDTO} from "./User";
import {RouteDTO} from "./RouteDTO";
import {Payment} from "./Payment";

export class RideDTO {
  id!: number;
  rideState!: string;
  route!: RouteDTO;
  payment!: Payment;
  driver!: UserDTO;
  // scheduledStart: ;
  // actualStart;
  // actualEnd;
  // reservationTime;
  customers!: Customer[];
  // carReviews;
  //  driverReviews;
}
