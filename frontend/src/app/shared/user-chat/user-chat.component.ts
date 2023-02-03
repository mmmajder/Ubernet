import {Component, OnDestroy, OnInit} from '@angular/core';
import {MessageService} from "../../services/message.service";
import {WebsocketService} from "../../services/websocket.service";
import {AuthService} from "../../services/auth.service";
import {Message} from "../../model/Message";
import {User} from "../../model/User";

@Component({
  selector: 'app-user-chat',
  templateUrl: './user-chat.component.html',
  styleUrls: ['./user-chat.component.css']
})
export class UserChatComponent implements OnInit, OnDestroy {
  messagesWithAdmin: Message[] = [];
  loggedUser: User;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService: AuthService) {
  }

  ngOnInit(): void {
    console.log("init user chat")
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;

      if (this.loggedUser.role !== "ADMIN") {
        this.webSocketService.openWebSocket(data.email, false, this.onNewMessageFromWebSocket.bind(this));
        this.loadMessages();
      }
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.closeWebSocket();
  }

  public onNewMessageFromWebSocket(message: Message): void {
    this.messagesWithAdmin.push(message);
  }

  private loadMessages(): void {
    this.messageService.getMessagesForClientEmail(this.loggedUser.email).subscribe(data => {
      for (const m of data) {
        this.messagesWithAdmin.push(m);
      }
    });
  }
}
