import {SimpleUser} from "./User";

export class Review {
  reviewer: SimpleUser;
  rating: number;
  comment: string;
  date: string;
}

export class CreateReview {
  comment: string;
  carRating: number;
  driverRating: number;
  rideId: number;
  clientEmail: string;

  constructor(comment: string, clientEmail: string, carRating: number, driverRating: number, rideId: number) {
    this.comment = comment;
    this.clientEmail = clientEmail;
    this.carRating = carRating;
    this.driverRating = driverRating;
    this.rideId = rideId;
  }
}

export class RideReview {
  id: number;
  customer: SimpleUser;
  carRating: number;
  driverRating: number;
  comment: string;
}

export class Comment {
  userEmail: string;
  adminEmail: string;
  content: string;
  time: number[];
}
