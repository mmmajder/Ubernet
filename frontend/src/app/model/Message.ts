// this.client = client;
// this.adminEmail = adminEmail;
// this.isSentByAdmin = isSentByAdmin;
// this.content = content;
// this.time = LocalDateTime.now();
// this.isDeleted = false;

export class Message {
  clientEmail !: string;
  adminEmail: string;
  sentByAdmin: boolean;
  content!: string;
  time: string;

  constructor(clientEmail : string, adminEmail: string, isSentByAdmin: boolean, content: string, time:string) {
    this.clientEmail  = clientEmail ;
    this.adminEmail = adminEmail;
    this.sentByAdmin = isSentByAdmin;
    this.content = content;
    this.time = time;
  }
}
