import {Component, Input, OnInit} from '@angular/core';
import {WebsocketService} from "../../services/websocket.service";
import {Message} from "../../model/Message";

@Component({
  selector: 'app-new-message',
  templateUrl: './new-message.component.html',
  styleUrls: ['./new-message.component.css']
})
export class NewMessageComponent implements OnInit {

  @Input() loggedUser: any;
  messageText: string = "";

  constructor(private webSocketService: WebsocketService) {
  }

  ngOnInit(): void {
  }

  sendMessage() {
    let message:Message = new Message("petar@gmail.com", "admin@gmail.com", true, this.messageText);
    this.webSocketService.sendMessage(message);
    this.messageText = "";
  }
}
