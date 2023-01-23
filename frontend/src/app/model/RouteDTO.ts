import {Place} from "./Position";
import {NumberOfRoute} from "./NumberOfRoute";

export class RouteDTO {
  id!: number;
  time!: number;
  price!: number;
  checkPoints: Place[];
  numberOfRoute: NumberOfRoute[]
}
