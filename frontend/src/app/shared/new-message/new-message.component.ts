import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WebsocketService} from "../../services/websocket.service";
import {Message} from "../../model/Message";
import {dateTimeNowToString} from "../../services/utils.service";

@Component({
  selector: 'app-new-message',
  templateUrl: './new-message.component.html',
  styleUrls: ['./new-message.component.css']
})
export class NewMessageComponent implements OnInit {

  @Input() loggedUser: any;
  @Input() messages: Message[];
  @Input() clientEmail:string;
  @Output() onNewMessageSent = new EventEmitter<Message>();
  messageText = "";

  constructor(private webSocketService: WebsocketService) {
  }

  ngOnInit(): void {
  }

  public sendMessage(): void {
    if (this.messageText.trim() !== ""){
      const message:Message = this.createMessage();
      this.webSocketService.sendMessage(message);
      this.messages.push(message);
      this.onNewMessageSent.emit(message);
      this.messageText = "";
    }
  }

  private createMessage(): Message{
    let message:Message;

    if (this.loggedUser.role === "ADMIN"){
      message = new Message(this.clientEmail, this.loggedUser.email, true, this.messageText, dateTimeNowToString());
    }
    else{
      message = new Message(this.loggedUser.email, "", false, this.messageText, dateTimeNowToString());
    }

    return message;
  }
}
