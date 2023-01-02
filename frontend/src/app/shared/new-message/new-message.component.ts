import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WebsocketService} from "../../services/websocket.service";
import {Message} from "../../model/Message";
import {MatTooltipModule} from '@angular/material/tooltip';

@Component({
  selector: 'app-new-message',
  templateUrl: './new-message.component.html',
  styleUrls: ['./new-message.component.css']
})
export class NewMessageComponent implements OnInit {

  @Input() loggedUser: any;
  @Input() messages: Message[];
  // @Output() onNewMessageSent:EventEmitter<Message> = new EventEmitter();
  messageText: string = "";

  constructor(private webSocketService: WebsocketService) {
  }

  ngOnInit(): void {
  }

  sendMessage() {
    console.log(this.loggedUser);
    let message:Message;

    if (this.loggedUser.role === "ADMIN"){
      message = new Message("petar@gmail.com", this.loggedUser.email, true, this.messageText);
    }
    else{
      message = new Message(this.loggedUser.email, "", false, this.messageText);
    }
    console.log("new message")
    console.log(message)
    this.webSocketService.sendMessage(message);
    console.log("poslato na ws")
    this.messages.push(message);
    console.log("stavljena u messages")
    console.log(this.messages)
    // this.onNewMessageSent.emit(message);
    // console.log("Emitovana poruka")
    this.messageText = "";
    console.log("ispraznjen new message content")
  }
}
