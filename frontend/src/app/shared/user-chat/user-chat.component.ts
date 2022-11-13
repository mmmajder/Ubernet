import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-user-chat',
  templateUrl: './user-chat.component.html',
  styleUrls: ['./user-chat.component.css']
})
export class UserChatComponent implements OnInit {
  messages = [{
    "profileImage": "assets/taxi.jpg",
    "text": "E imao sam pitanje.. wegbzewugzewze iugvzewfbweizg zufvewi.",
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

  constructor() {
  }

  ngOnInit(): void {
  }

}
