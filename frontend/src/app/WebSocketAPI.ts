import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {NavbarComponent} from "./shared/navbar/navbar/navbar.component";

export class WebSocketAPI {
  webSocketEndPoint: string = 'http://localhost:8081/ws';
  topic: string = "/topic/created-restaurant";
  stompClient: any;
  container: NavbarComponent;

  constructor(container: NavbarComponent) {
    this.container = container;
  }

  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;
    _this.stompClient.connect({}, function () {
      _this.stompClient.subscribe(_this.topic, function (sdkEvent: any) {
        _this.onMessageReceived(sdkEvent);
      });
    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  errorCallBack(error: string) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  /**
   * Send message to sever via web socket
   * @param {*} message
   * @param endpoint
   */
  _send(message: any, endpoint: string) {
    console.log("calling logout api via web socket");
    this.stompClient.send(endpoint, {}, message);
  }

  onMessageReceived(message: any) {
    console.log("Message Received from Server :: " + message);
    setTimeout(() => {
      this.container.handleMessage(message);
    }, 2000);
  }

}
