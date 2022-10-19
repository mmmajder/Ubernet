import {Injectable} from "@angular/core";
import {WebSocketAPI} from "../WebSocketAPI";
import {NavbarComponent} from "../shared/navbar/navbar/navbar.component";

@Injectable({
  providedIn: 'root'
})
export class SocketService {

  webSocketAPI!: WebSocketAPI;

  createWebSocket(navbarComponent: NavbarComponent) {
    this.webSocketAPI = new WebSocketAPI(navbarComponent);
    this.webSocketAPI._connect();
  }
}
