import {SimpleUser} from "./User";

export class Review {
  reviewer: SimpleUser;
  rating: number;
  comment: string;
  date: string;
}

export class Comment {
  userEmail: string;
  adminEmail: string;
  content: string;
  time: number[];
}
