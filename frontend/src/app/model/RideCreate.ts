import {LatLngDTO} from "./LatLngDTO";
import {InstructionDTO} from "./InstructionDTO";
import {Place} from "./Position";
import {PaymentDTO} from "./PaymentDTO";

export class RideCreate {
  coordinates: LatLngDTO[] = []
  instructions: InstructionDTO[] = []
  carType: string
  hasPet: boolean
  hasChild: boolean
  passengers: string[]
  totalDistance = 0
  totalTime = 0
  reservationTime: string;
  route: (Place | null)[];
  numberOfRoute: number[] = [];
  payment: PaymentDTO;
  reservation: boolean;
}
