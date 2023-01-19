import {CarTypeGetResponse} from "./CarTypeGetResponse";
import {CurrentRide} from "./CurrentRide";
import {Position} from "./Position";
import {Navigation} from "./Navigation";

export class Car {
  id!: number;
  plates!: string;
  name!: string;
  carType!: CarTypeGetResponse;
  allowsBaby!: boolean;
  allowsPet!: boolean;
  driverEmail!: string;
  driver!: any;
  navigation!: Navigation
  position!: Position

  constructor(id: number, name: string, plates: string, carType: CarTypeGetResponse, allowsBabies: boolean, allowsPets: boolean,
              driverEmail: string) {
    this.name = name;
    this.plates = plates;
    this.carType = carType;
    this.allowsBaby = allowsBabies;
    this.allowsPet = allowsPets;
    this.id = id;
    this.driverEmail = driverEmail;
  }
}
