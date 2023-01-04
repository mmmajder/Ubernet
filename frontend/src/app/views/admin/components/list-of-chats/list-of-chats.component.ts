import {Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import {Message} from "../../../../model/Message";
import {Chat} from "../../../../model/Chat";
import {ImageService} from "../../../../services/image.service";

@Component({
  selector: 'app-list-of-chats',
  templateUrl: './list-of-chats.component.html',
  styleUrls: ['./list-of-chats.component.css']
})
export class ListOfChatsComponent implements OnInit {
  @Input() chats: Chat[];
  @Output() onChatSelection = new EventEmitter<Chat>();

  constructor(private imageService: ImageService) {
  }

  ngOnInit(): void {
  }

  // public getProfileImage(clientEmail:string) {
  //   return "assets/taxi.jpg";
  //   // console.log("getprofileimage")
  //   // this.imageService.getProfileImage(clientEmail)
  //   //   .subscribe((encodedImage: any) => {
  //   //     if (encodedImage === null)
  //   //       return "assets/taxi.jpg";
  //   //     else
  //   //       return `data:image/jpeg;base64,${encodedImage.data}`;
  //   //   });
  // }

  selectChat(email: string) {
    // console.log("selectChat")
    for (let c of this.chats){
      if (c.clientEmail === email){
        this.onChatSelection.emit(c);
        console.log("emit(chat) for " + email)
      }
    }
  }

}
