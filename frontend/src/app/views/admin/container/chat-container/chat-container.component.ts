import { Component, OnInit } from '@angular/core';
import {WebsocketService} from "../../../../services/websocket.service";
import {MessageService} from "../../../../services/message.service";
import {AuthService} from "../../../../services/auth.service";

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrls: ['./chat-container.component.css']
})
export class ChatContainerComponent implements OnInit {

  loggedUser: any = null;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService:AuthService) { }

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;
      this.webSocketService.openWebSocket(data.email, true);
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.closeWebSocket();
  }

}
