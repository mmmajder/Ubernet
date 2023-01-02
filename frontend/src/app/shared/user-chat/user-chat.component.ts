import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../../services/message.service";
import {WebsocketService} from "../../services/websocket.service";
import {AuthService} from "../../services/auth.service";
import {Message} from "../../model/Message";

@Component({
  selector: 'app-user-chat',
  templateUrl: './user-chat.component.html',
  styleUrls: ['./user-chat.component.css']
})
export class UserChatComponent implements OnInit {
  messagesWithAdmin: Message[] = [];
  // messages = [{
  //   "profileImage": "assets/taxi.jpg",
  //   "text": "E imao sam pitanje.. wegbzewugzewze iugvzewfbweizg zufvewi.",
  //   "time": "20:00 12.10.2022.",
  //   "type": "right"
  // },
  //   {
  //     "profileImage": "assets/taxi.jpg",
  //     "text": "E imao sam pitanje...",
  //     "time": "20:00 12.10.2022.",
  //     "type": "right"
  //   },
  //   {
  //     "profileImage": "assets/taxi.jpg",
  //     "text": "E imao sam pitanje...",
  //     "time": "20:00 12.10.2022.",
  //     "type": "right"
  //   },
  //   {
  //     "profileImage": "assets/taxi.jpg",
  //     "text": "E imao sam pitanje...",
  //     "time": "20:00 12.10.2022.",
  //     "type": "left"
  //   },];

  loggedUser: any = null;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService:AuthService) { }

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;
      this.webSocketService.openWebSocket(data.email, false, this.messagesWithAdmin);
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.closeWebSocket();
  }

}
