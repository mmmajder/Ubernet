export class CarTypeGetResponse {
  id!: number;
  name!: string;
  priceForType!: number;
  deleted!: boolean;

  constructor(name: string) {
    this.name = name
  }

}
