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

  public openWebSocket(email: string, isAdmin: boolean, onNewMessage: Function) {
    if (!this.isWebSocketOpen) {
      this.isWebSocketOpen = true;
      this.email = email;
      this.isAdmin = isAdmin;
      this.webSocket = new WebSocket(this.chatWSUrl + email);
      this.webSocket.onmessage = (event) => {
        const message: Message = JSON.parse(event.data);
        onNewMessage(message);
      }
      this.webSocket.onopen = () => {
      }
      this.webSocket.onclose = (event) => {
        // console.log("Close " + email, event)
      }
    }
  }

  public sendMessage(message: any) {
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
