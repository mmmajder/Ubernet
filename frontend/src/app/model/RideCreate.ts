import {LatLngDTO} from "./LatLngDTO";
import {InstructionDTO} from "./InstructionDTO";

export class RideCreate {
  coordinates: LatLngDTO[]
  instructions: InstructionDTO[]
  carType: string
  totalDistance: number
  totalTime: number
  hasPet: boolean
  hasChild: boolean
  passengers: String[]
  reservationTime: String;
}
