import {CarTypeGetResponse} from "./CarTypeGetResponse";
import {Position} from "./Position";
import {Navigation} from "./Navigation";
import {DriverDTO} from "./DriverDTO";

export class Car {
  id!: number;
  plates!: string;
  name!: string;
  carType!: CarTypeGetResponse;
  allowsBaby!: boolean;
  allowsPet!: boolean;
  driverEmail!: string;
  driver!: DriverDTO;
  navigation!: Navigation;
  position!: Position;

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
