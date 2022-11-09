import {Component, OnInit} from '@angular/core';
import {MessageService} from "../../../../services/message.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  messages = [{
    "profileImage": "assets/taxi.jpg",
    "text": "E imao sam pitanje...",
    "time": "20:00 12.10.2022.",
    "type": "right"
  },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
      "time": "20:00 12.10.2022.",
      "type": "right"
    },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
      "time": "20:00 12.10.2022.",
      "type": "right"
    },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
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
    console.log("tu samm");

    this.messageService.getTest().subscribe((data) => {
      this.testStr = data;
      console.log(this.testStr);
    });

    console.log("tu samm2");

    this.messageService.getMessages("petar@gmail.com").subscribe((data) => {
      this.messagesFromServer = data;
      console.log(this.messagesFromServer);
    });
  }
}
