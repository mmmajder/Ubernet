import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../../model/Message";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {
  @Input() message: Message;
  @Input() isUserAdmin:boolean;
  isSentByTheUser:boolean; // depending upon if the message was sent or received the message will be rendered on right or left side of the chat
  hideTime: boolean = true;

  constructor() {

  }

  ngOnInit(): void {
    console.log("ispisuje se nova poruka")
    console.log(this.message);
    this.isSentByTheUser = false;
    if (this.message.sentByAdmin === this.isUserAdmin){
      this.isSentByTheUser = true;
    }

    if (this.isSentByTheUser){
      console.log("generisem poslatu poruku")
    } else {
      console.log("generisem primljenu poruku")
    }

    console.log(this.message);
  }

}
