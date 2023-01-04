import { Component, OnInit } from '@angular/core';
import {WebsocketService} from "../../../../services/websocket.service";
import {MessageService} from "../../../../services/message.service";
import {AuthService} from "../../../../services/auth.service";
import {Message} from "../../../../model/Message";
import {Chat} from "../../../../model/Chat";
import {UserService} from "../../../../services/user.service";
import {ImageService} from "../../../../services/image.service";

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrls: ['./chat-container.component.css']
})
export class ChatContainerComponent implements OnInit {

  chats:Chat[] = [];
  loggedUser: any = null;
  clientName:string;
  clientEmail:string;
  messagesWithClient:Message[] = [];
  openChatProfilePicture:string;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService:AuthService
              , private userService:UserService, private imageService: ImageService) { }

  ngOnInit(): void {
    console.log("init admin")
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;

      this.messageService.getChats().subscribe(chats => {
        for (let c of chats){
          this.chats.push(c);
        }
        this.webSocketService.openWebSocket(data.email, true, this.onNewMessageFromWebSocket.bind(this));
        this.openMostRecentChat();
      });
    });
  }

  ngOnDestroy(): void {
    this.webSocketService.closeWebSocket();
  }

  public onNewMessageFromWebSocket(message: Message): void {
    // add message to the current chat
    console.log("on new message")
    console.log(message)
    if (message.clientEmail === this.clientEmail){
      this.messagesWithClient.push(message);
      this.putCurrentChatAsFirst(message);
      return;
    } else { // reorder the chats (on the left)
      for (let i=0; i < this.chats.length; i++) {
        let c:Chat = this.chats[i];
        if (message.clientEmail === c.clientEmail){
          c.mostRecentMessage = message;
          this.moveChatToTheBeginning(i);
          return;
        }
      }
    }
    // add message as a new chat
    // TODO test when user-chat disappearance is fixed
    this.userService.getUserFullname(message.clientEmail).subscribe(data => {
      let fullname:string = data.name + " " + data.lastname;
      let c:Chat = new Chat(message.clientEmail, fullname, message);
      this.chats.unshift(c);
    });
  }

  private moveChatToTheBeginning(index:number): void{
    let c:Chat = this.chats.splice(index,1)[0]; // removes the chat on index 'i' (the chat that got new message)
    this.chats.unshift(c); // adds the chat to the beginning of the chats array
  }

  public openMostRecentChat(): void{
    if (this.chats.length > 0){
      let selectedChat:Chat = this.chats[0];
      this.clientName = selectedChat.clientFullname;
      this.clientEmail = selectedChat.clientEmail;
      this.loadChatMessages();
      this.getOpenChatProfilePicture();
    }
  }

  public addNewMessageToMessages(newMessage:Message):void {
    this.messagesWithClient.push(newMessage);
  }

  public openSelectedChat(selectedChat:Chat): void{
    if (selectedChat.clientEmail !== this.clientEmail){
      this.prepareForSelectedChat(selectedChat);
      //TODO change profile photo
      this.loadChatMessages();
      this.getOpenChatProfilePicture();
    }
  }

  public putCurrentChatAsFirst(newSentMessage:Message): void{
    for (let i=0; i < this.chats.length; i++) {
      let c:Chat = this.chats[i];
      if (this.clientEmail === c.clientEmail){
        c.mostRecentMessage = newSentMessage;
        this.moveChatToTheBeginning(i);
        return;
      }
    }
  }

  private prepareForSelectedChat(selectedChat:Chat): void{
    this.clientEmail = selectedChat.clientEmail;
    this.clientName = selectedChat.clientFullname;
    this.messagesWithClient.length = 0;
  }

  private loadChatMessages():void {
    this.messageService.getMessagesForClientEmail(this.clientEmail).subscribe(previousMessages => {
      for (let m of previousMessages){
        this.messagesWithClient.push(m);
      }
    });
  }

  private getOpenChatProfilePicture():void{
    this.imageService.getProfileImage(this.clientEmail)
      .subscribe((encodedImage: any) => {
        console.log(encodedImage);
        if (encodedImage === null)
          this.openChatProfilePicture = "assets/taxi.jpg";
        else
          this.openChatProfilePicture =  `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }

}
