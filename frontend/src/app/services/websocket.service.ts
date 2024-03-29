import {Injectable} from "@angular/core";
import {Message} from "../model/Message";

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  webSocket: WebSocket;
  email: string;
  isAdmin: boolean;
  isWebSocketOpen = false;

  private readonly chatWSUrl: string;

  constructor() {
    this.chatWSUrl = 'ws://localhost:8000/chatWebSocket/';
  }

  public openWebSocket(email: string, isAdmin: boolean, onNewMessage: (arg0:any)=> void) {
    if (!this.isWebSocketOpen) {
      this.isWebSocketOpen = true;
      this.email = email;
      this.isAdmin = isAdmin;
      this.webSocket = new WebSocket(this.chatWSUrl + email);

      this.webSocket.onopen = () => {
        console.log("websocket open");
      }

      this.webSocket.onmessage = (event) => {
        const message: Message = JSON.parse(event.data);
        onNewMessage(message);
      }

      this.webSocket.onclose = (event:CloseEvent) => {
        console.log("Close " + email, event)
      }
    }
  }

  public sendMessage(message: Message) {
    if (this.isWebSocketOpen) {
      this.webSocket.send(JSON.stringify(message));
    }
  }

  public closeWebSocket() {
    if (this.isWebSocketOpen) {
      this.isWebSocketOpen = false;
      this.webSocket.close();
    }
  }
}
