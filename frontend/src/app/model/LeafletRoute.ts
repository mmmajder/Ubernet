import {LatLngDTO} from "./LatLngDTO";
import {InstructionDTO} from "./InstructionDTO";

export class LeafletRoute {
  coordinates: LatLngDTO[]
  instructions: InstructionDTO[]
  name: string
  routesIndex: number
  summary: LeafletRouteSummary
}

export class LeafletRouteSummary {
  totalDistance: number
  totalTime: number
}
