import {SimpleUser} from "./User";

export class Review {
  reviewer: SimpleUser;
  rating: number;
  comment: string;
  date: string;
}

export class Comment {
  reviewer: SimpleUser;
  comment: string;
  date: string;
}
