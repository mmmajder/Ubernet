import {Component, OnDestroy, OnInit} from '@angular/core';
import {WebsocketService} from "../../../../services/websocket.service";
import {MessageService} from "../../../../services/message.service";
import {AuthService} from "../../../../services/auth.service";
import {Message} from "../../../../model/Message";
import {Chat} from "../../../../model/Chat";
import {UserService} from "../../../../services/user.service";
import {EncodedImage, ImageService} from "../../../../services/image.service";
import {User} from "../../../../model/User";

@Component({
  selector: 'app-chat-container',
  templateUrl: './chat-container.component.html',
  styleUrls: ['./chat-container.component.css']
})
export class ChatContainerComponent implements OnInit, OnDestroy {

  chats: Chat[] = [];
  loggedUser: User;
  clientName: string;
  clientEmail: string;
  messagesWithClient: Message[] = [];
  openChatProfilePicture: string;

  constructor(private messageService: MessageService, private webSocketService: WebsocketService, private authService: AuthService, private userService: UserService, private imageService: ImageService) {
  }

  ngOnInit(): void {
    this.authService.getCurrentlyLoggedUser().subscribe(data => {
      this.loggedUser = data;

      this.messageService.getChats().subscribe(chats => {
        for (const c of chats) {
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
    if (message.clientEmail === this.clientEmail) {
      this.messagesWithClient.push(message);
      this.putCurrentChatAsFirst(message);
      return;
    } else { // reorder the chats (on the left)
      for (let i = 0; i < this.chats.length; i++) {
        const c: Chat = this.chats[i];
        if (message.clientEmail === c.clientEmail) {
          c.mostRecentMessage = message;
          this.moveChatToTheBeginning(i);
          return;
        }
      }
    }
    // add message as a new chat
    // TODO test when user-chat disappearance is fixed
    this.userService.getUserFullName(message.clientEmail).subscribe(data => {
      const fullName: string = data.name + " " + data.lastname;
      const c: Chat = new Chat(message.clientEmail, fullName, message);
      this.chats.unshift(c);
    });
  }

  private moveChatToTheBeginning(index: number): void {
    const c: Chat = this.chats.splice(index, 1)[0]; // removes the chat on index 'i' (the chat that got new message)
    this.chats.unshift(c); // adds the chat to the beginning of the chats array
  }

  public openMostRecentChat(): void {
    if (this.chats.length > 0) {
      const selectedChat: Chat = this.chats[0];
      this.clientName = selectedChat.clientFullname;
      this.clientEmail = selectedChat.clientEmail;
      this.loadChatMessages();
      this.getOpenChatProfilePicture();
    }
  }

  public addNewMessageToMessages(newMessage: Message): void {
    this.messagesWithClient.push(newMessage);
  }

  public openSelectedChat(selectedChat: Chat): void {
    if (selectedChat.clientEmail !== this.clientEmail) {
      this.prepareForSelectedChat(selectedChat);
      //TODO change profile photo
      this.loadChatMessages();
      this.getOpenChatProfilePicture();
    }
  }

  public putCurrentChatAsFirst(newSentMessage: Message): void {
    for (let i = 0; i < this.chats.length; i++) {
      const c: Chat = this.chats[i];
      if (this.clientEmail === c.clientEmail) {
        c.mostRecentMessage = newSentMessage;
        this.moveChatToTheBeginning(i);
        return;
      }
    }
  }

  private prepareForSelectedChat(selectedChat: Chat): void {
    this.clientEmail = selectedChat.clientEmail;
    this.clientName = selectedChat.clientFullname;
    this.messagesWithClient.length = 0;
  }

  private loadChatMessages(): void {
    this.messageService.getMessagesForClientEmail(this.clientEmail).subscribe(previousMessages => {
      for (const m of previousMessages) {
        this.messagesWithClient.push(m);
      }
    });
  }

  private getOpenChatProfilePicture(): void {
    console.log("trazim sliku u chat container")
    this.imageService.getProfileImage(this.clientEmail)
      .subscribe((encodedImage: EncodedImage) => {
        console.log(encodedImage);
        if (encodedImage === null)
          this.openChatProfilePicture = "assets/default-profile-picture.jpg";
        else
          this.openChatProfilePicture = `data:image/jpeg;base64,${encodedImage.data}`;
      });
  }
}
