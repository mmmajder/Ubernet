import {Position} from "./Position";
import {PositionInTime} from "./PositionInTime";

export class CurrentRide {
  currenRideId!: number;
  positions!: PositionInTime[];
  currentRide!: any
  numberOfRoute!: number
  freeRide: boolean
}
