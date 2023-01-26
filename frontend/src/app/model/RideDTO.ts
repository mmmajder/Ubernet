import {Customer, UserDTO} from "./User";
import {RouteDTO} from "./RouteDTO";
import {Payment} from "./Payment";

export class RideDTO {
  id!: number;
  rideState!: string;
  route!: RouteDTO;
  payment!: Payment;
  driver!: UserDTO;
  customers!: Customer[];
}
