import {Injectable} from "@angular/core";
import {Message} from "../model/Message";
import {from, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  webSocket: WebSocket;
  // message:Observable<Message>;
  email:string;
  isAdmin:boolean;
  isWebSocketOpen:boolean = false;


  private readonly chatWSUrl: string;

  constructor() {
    this.chatWSUrl = 'ws://localhost:8000/chatWebSocket/';
  }

  public openWebSocket(email:string, isAdmin:boolean, onNewMessage:Function){
    if (!this.isWebSocketOpen){
      this.isWebSocketOpen = true;
      this.email = email;
      this.isAdmin = isAdmin;
      // if (message !== undefined) {
      //   this.message = message;
      // }

      // if (this.webSocket === null || this.webSocket === undefined || this.webSocket.readyState !== this.webSocket.OPEN){
      this.webSocket = new WebSocket(this.chatWSUrl + email);

      this.webSocket.onopen = (event) => {
        console.log("Open "  + email, event)
      }

      this.webSocket.onmessage = (event) => {
        let message:Message = JSON.parse(event.data);
        console.log(message.content);

        onNewMessage(message);
        // this.message = of(message);
      }

      this.webSocket.onclose = (event) => {
        console.log("Close "  + email, event)
      }
      // }
    //
    }

  }

  public sendMessage(message:any){
    if (this.isWebSocketOpen){
      this.webSocket.send(JSON.stringify(message));
    }
    // if (this.webSocket !== null && this.webSocket !== undefined){
    //   this.webSocket.send(JSON.stringify(message));
    // }
  }

  public closeWebSocket(){
    if (this.isWebSocketOpen){
      this.isWebSocketOpen = false;
      this.webSocket.close();
    }
    // if (this.webSocket !== null && this.webSocket !== undefined){
    //   this.webSocket.close();
    // }
  }
}
