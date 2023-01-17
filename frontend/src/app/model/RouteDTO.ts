import {Place} from "./Position";

export class RouteDTO {
  id!: number;
  time!: number;
  price!: number;
  checkPoints: Place[];
}
