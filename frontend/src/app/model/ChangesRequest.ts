import {SimpleUser} from "./User";

export class ChangesRequestDTO {
  driver: SimpleUser;
  type = '';
}

export class ChangesRequest {
  driver: SimpleUser;
  type: string;
  changes: Map<string, string>;
  date: string;
}
