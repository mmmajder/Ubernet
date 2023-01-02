import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Message} from "../model/Message";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  webSocket: WebSocket;
  messages:Message[];
  email:string;
  isAdmin:boolean;


  private readonly chatWSUrl: string;

  constructor() {
    this.chatWSUrl = 'ws://localhost:8000/chatWebSocket/';
  }

  public openWebSocket(email:string, isAdmin:boolean, messages:Message[]){
    this.email = email;
    this.isAdmin = isAdmin;
    if (messages !== undefined)
      this.messages = messages;

    if (this.webSocket === null || this.webSocket === undefined || this.webSocket.readyState !== this.webSocket.OPEN){
      this.webSocket = new WebSocket(this.chatWSUrl + email);

      this.webSocket.onopen = (event) => {
        console.log("Open "  + email, event)
      }

      this.webSocket.onmessage = (event) => {
        let message:Message = JSON.parse(event.data);
        console.log(message.content);
        this.messages.push(message);
      }

      this.webSocket.onclose = (event) => {
        console.log("Close "  + email, event)
      }
    }

  }

  public sendMessage(message:any){
    if (this.webSocket !== null && this.webSocket !== undefined){
      this.webSocket.send(JSON.stringify(message));
    }
  }

  public closeWebSocket(){
    if (this.webSocket !== null && this.webSocket !== undefined){
      this.webSocket.close();
    }
  }
}
