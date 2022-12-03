import {CarTypeGetResponse} from "./CarTypeGetResponse";

export class CarSettings {
  name!: string;
  plates!: string;
  carType!: CarTypeGetResponse;
  allowsBabies!: boolean;
  allowsPets!: boolean;

  constructor(name: string, plates: string, carType: CarTypeGetResponse, allowsBabies: boolean, allowsPets: boolean) {
    this.name = name;
    this.plates = plates;
    this.carType = carType;
    this.allowsBabies = allowsBabies;
    this.allowsPets = allowsPets;
  }
}


