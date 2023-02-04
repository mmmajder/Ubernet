export interface FavoriteRouteItem {
  checkPoints: string[];
  rideId: number;
}

export class FavoriteRouteRequest {
  customerEmail: string;
  rideId: number;

  constructor(customerEmail: string, rideId: number) {
    this.customerEmail = customerEmail;
    this.rideId = rideId;
  }
}
