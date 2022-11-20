import {Position} from "./Position";

export class ActiveCarResponse {
  carId!:number;
  driverEmail!:string;
  destinations!:Position[];
  currentPosition!: Position;
}
