import {Message} from "./Message";

export class Chat {
  clientEmail !: string;
  clientFullname!: string;
  mostRecentMessage!: Message;

  constructor(clientEmail : string, clientFullname: string, mostRecentMessage: Message) {
    this.clientEmail  = clientEmail ;
    this.clientFullname = clientFullname;
    this.mostRecentMessage = mostRecentMessage;
  }
}
