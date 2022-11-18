import {Component, OnInit} from '@angular/core';
import {MessageService} from "../../../../services/message.service";

@Component({
  selector: 'app-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent implements OnInit {
  messages: any = [];
  testStr: any;

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    // this.messageService.getTest().subscribe((data) => {
    //   this.testStr = data;
    //   console.log(this.testStr);
    // });

    this.messageService.getMessagesAsAdmin("petar@gmail.com").subscribe((data) => {
      this.messages = data;
      console.log(data);
    });
  }
}
