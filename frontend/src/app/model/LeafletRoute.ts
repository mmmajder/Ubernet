import {InstructionDTO} from "./InstructionDTO";
import * as L from 'leaflet';

export class LeafletRoute {
  coordinates: L.LatLng[]
  instructions: InstructionDTO[]
  name: string
  routesIndex: number
  summary: LeafletRouteSummary
}

export class LeafletRouteSummary {
  totalDistance: number
  totalTime: number
}
