import {Component, OnInit} from '@angular/core';
import {MessageService} from "../../../../services/message.service";

@Component({
  selector: 'app-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
<<<<<<< HEAD:frontend/src/app/views/admin/components/chat/chat.component.ts
export class ChatComponent implements OnInit {
  messages: any = [{
=======
export class AdminChatComponent implements OnInit {
  messages = [{
>>>>>>> dev:frontend/src/app/views/admin/components/admin-chat/admin-chat.component.ts
    "profileImage": "assets/taxi.jpg",
    "content": "E imao sam pitanje...",
    "time": "20:00 12.10.2022.",
    "type": "right"
  },
    {
      "profileImage": "assets/taxi.jpg",
      "content": "E imao sam pitanje...",
      "time": "20:00 12.10.2022.",
      "type": "right"
    },
    {
      "profileImage": "assets/taxi.jpg",
      "content": "E imao sam pitanje...",
      "time": "20:00 12.10.2022.",
      "type": "right"
    },
    {
      "profileImage": "assets/taxi.jpg",
      "content": "E imao sam pitanje...",
      "time": "20:00 12.10.2022.",
      "type": "left"
    },];

  constructor(private messageService: MessageService) {}

  // messageService: MessageService
  //
  // constructor(messageService: MessageService) {
  //   this.messageService = messageService;
  // }

  messagesFromServer: any;
  testStr: any;

  ngOnInit(): void {
    this.messageService.getTest().subscribe((data) => {
      this.testStr = data;
      console.log(this.testStr);
    });

    this.messageService.getMessagesAsAdmin("petar@gmail.com").subscribe((data) => {
      this.messagesFromServer = data;
      this.messages = data;
      console.log(this.messagesFromServer);
    });
  }
}
