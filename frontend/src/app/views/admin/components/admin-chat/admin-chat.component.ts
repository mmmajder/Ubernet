import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent implements OnInit {
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

  constructor() {
  }

  ngOnInit(): void {
  }

}
