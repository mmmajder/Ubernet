import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-list-of-chats',
  templateUrl: './list-of-chats.component.html',
  styleUrls: ['./list-of-chats.component.css']
})
export class ListOfChatsComponent implements OnInit {
  chats = [{
      "profileImage": "assets/taxi.jpg",
      "name": "Pera Peric",
      "lastMessage": "E imao sam pitanje...",
    },
    {
      "profileImage": "assets/taxi.jpg",
      "name": "Pera Peric",
      "lastMessage": "E imao sam pitanje..."
    },
    {
      "profileImage": "assets/taxi.jpg",
      "name": "Pera Peric",
      "lastMessage": "E imao sam pitanje..."
    },
    {
      "profileImage": "assets/taxi.jpg",
      "name": "Pera Peric",
      "lastMessage": "E imao sam pitanje..."
    },
    {
      "profileImage": "assets/taxi.jpg",
      "name": "Pera Peric",
      "lastMessage": "E imao sam pitanje..."
    }];

  constructor() {
  }

  ngOnInit(): void {
  }

}
