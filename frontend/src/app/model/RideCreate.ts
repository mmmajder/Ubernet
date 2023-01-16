import {LatLngDTO} from "./LatLngDTO";
import {InstructionDTO} from "./InstructionDTO";
import {Place} from "./Position";
import {PaymentDTO} from "./PaymentDTO";

export class RideCreate {
  coordinates: LatLngDTO[]
  instructions: InstructionDTO[]
  carType: string
  hasPet: boolean
  hasChild: boolean
  passengers: String[]
  totalDistance: number
  totalTime: number
  reservationTime: String;
  route: (Place | null)[];
  numberOfRoute: number;
  payment: PaymentDTO;
}
