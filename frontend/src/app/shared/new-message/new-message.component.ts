import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WebsocketService} from "../../services/websocket.service";
import {Message} from "../../model/Message";
import {MatTooltipModule} from '@angular/material/tooltip';
import {Chat} from "../../model/Chat";

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
  messageText: string = "";

  constructor(private webSocketService: WebsocketService) {
  }

  ngOnInit(): void {
  }

  public sendMessage(): void {
    if (this.messageText.trim() !== ""){
      let message:Message = this.createMessage();
      this.webSocketService.sendMessage(message);
      this.messages.push(message);
      this.onNewMessageSent.emit(message);
      this.messageText = "";
    }
  }

  private createMessage(): Message{
    let message:Message;

    if (this.loggedUser.role === "ADMIN"){
      message = new Message(this.clientEmail, this.loggedUser.email, true, this.messageText);
    }
    else{
      message = new Message(this.loggedUser.email, "", false, this.messageText);
    }

    return message;
  }
}
