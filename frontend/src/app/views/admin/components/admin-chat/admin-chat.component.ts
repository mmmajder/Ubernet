import {Component, Input} from '@angular/core';
import {Message} from "../../../../model/Message";
import {User} from "../../../../model/User";

@Component({
  selector: 'app-chat',
  templateUrl: './admin-chat.component.html',
  styleUrls: ['./admin-chat.component.css']
})
export class AdminChatComponent {
  @Input() messages: Message[];
  @Input() loggedUser: User;
}
