import { Component, OnInit } from '@angular/core';
import {WebsocketService} from "../../../../services/websocket.service";
import {MessageService} from "../../../../services/message.service";
import {AuthService} from "../../../../services/auth.service";
import {Message} from "../../../../model/Message";

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrls: ['./chat-container.component.css']
})
export class ChatContainerComponent implements OnInit {

  loggedUser: any = null;
  clientName:string = "Pera Peric";
  clientEmail:string = "petar@gmail.com"
  messagesWithClient:Message[] = [];

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService:AuthService) { }

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;
      this.webSocketService.openWebSocket(data.email, true, this.messagesWithClient);
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.closeWebSocket();
  }

  public addNewMessageToMessages(newMessage:Message):void {
    console.log("Dobio novu poruku preko eventa")
    console.log(newMessage)
    this.messagesWithClient.push(newMessage);
    console.log("Dodao novu poruku u messages")
    console.log(this.messagesWithClient)
  }

}
