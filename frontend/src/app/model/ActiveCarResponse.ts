import {Position} from "./Position";
import {CurrentRide} from "./CurrentRide";

export class ActiveCarResponse {
  carId!: number;
  driverEmail!: string;
  currentPosition!: Position;
  currentRide!: CurrentRide
  approachFirstRide: CurrentRide
  firstRide: CurrentRide
}
