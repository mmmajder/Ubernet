import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  webSocket: WebSocket;
  messages:any[] = [];
  email:string;
  isAdmin:boolean;

  private readonly chatWSUrl: string;

  constructor() {
    this.chatWSUrl = 'ws://localhost:8000/chatWebSocket/';
  }

  public openWebSocket(email:string, isAdmin:boolean){
    this.email = email;
    this.isAdmin = isAdmin;
    this.webSocket = new WebSocket(this.chatWSUrl + email);

    this.webSocket.onopen = (event) => {
      console.log("Open " + email,  event)
    }

    this.webSocket.onmessage = (event) => {
      let message:any = JSON.parse(event.data);

      if ((this.isAdmin && this.email !== message.adminEmail) || !this.isAdmin){
        console.log("Stigla poruka sa servera");
        console.log(message.content);
      }

    }

    this.webSocket.onclose = (event) => {
      console.log("Close "  + email, event)
    }
  }

  public sendMessage(message:any){
    this.webSocket.send(JSON.stringify(message));
  }

  public closeWebSocket(){
    this.webSocket.close();
  }
}
