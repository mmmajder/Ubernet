import {SimpleUser} from "./User";

export class Review {
  reviewer: SimpleUser;
  rating: number;
  comment: string;
  date: string;
}

export class CreateReview {
  comment: string;
  rating: number;
  rideId: number;
  clientEmail: string;

  constructor(comment: string, clientEmail: string, rating: number, rideId: number) {
    this.comment = comment;
    this.clientEmail = clientEmail;
    this.rating = rating;
    this.rideId = rideId;
  }
}

export class RideReview {
  id: number;
  customer: SimpleUser;
  rating: number;
  comment: string;
}

export class Comment {
  userEmail: string;
  adminEmail: string;
  content: string;
  time: number[];
}
