import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  messages = [{
    "profileImage": "assets/taxi.jpg",
    "text": "E imao sam pitanje...",
  },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
    },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
    },
    {
      "profileImage": "assets/taxi.jpg",
      "text": "E imao sam pitanje...",
    },];

  constructor() {
  }

  ngOnInit(): void {
  }

}
