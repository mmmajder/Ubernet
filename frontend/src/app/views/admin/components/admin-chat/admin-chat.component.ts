import {Component, Input, OnInit} from '@angular/core';
import {MessageService} from "../../../../services/message.service";
import {WebsocketService} from "../../../../services/websocket.service";
import {AuthService} from "../../../../services/auth.service";
import {Message} from "../../../../model/Message";

@Component({
  selector: 'app-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent implements OnInit {
  @Input() messages: Message[];
  testStr: any;
  @Input() loggedUser: any;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService:AuthService) {}

  ngOnInit(): void {
    // this.messageService.getTest().subscribe((data) => {
    //   this.testStr = data;
    //   console.log(this.testStr);
    // });
    // this.authService.getCurrentlyLoggedUser().subscribe(data => {
    //   this.loggedUser = data;
    //   // this.webSocketService.openWebSocket(data.email);
    // });
    //
    // this.messageService.getMessagesAsAdmin("petar@gmail.com").subscribe((data) => {
    //   this.messages = data;
    //   console.log(data);
    // });
  }

  // ngOnDestroy(): void {
  //   this.webSocketService.closeWebSocket();
  // }
}
