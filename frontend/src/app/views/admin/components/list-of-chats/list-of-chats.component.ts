import {
  Component,
  Input,
  OnInit,
  Output,
  EventEmitter,
  IterableDiffer, IterableDiffers
} from '@angular/core';
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
  iterableDiffer: IterableDiffer<Chat>;
  profilePictures: any; // {'email':'image_src'}
  hasRequestedProfilePictures: any; // {'email': true}

  constructor(private imageService: ImageService, private iterableDiffers: IterableDiffers) {
    // this.iterableDiffer = this.iterableDiffers.find(this.chats).create();
  }

  ngOnInit(): void {
    this.profilePictures = {};
    this.hasRequestedProfilePictures = {};
  }

  ngOnDestroy(): void {
    this.profilePictures = {};
    this.hasRequestedProfilePictures = {};
  }

  ngDoCheck() {
    this.getProfileImages(this.chats);
  }

  public getProfileImages(chats: Chat[]): void {
    for (const c of chats) {
      if (!this.hasRequestedProfilePictures.hasOwnProperty(c.clientEmail) && !this.profilePictures.hasOwnProperty(c.clientEmail)) {
        this.hasRequestedProfilePictures[c.clientEmail] = true;
        this.imageService.getProfileImage(c.clientEmail)
          .subscribe((encodedImage: any) => {
            if (encodedImage === null)
              this.profilePictures[c.clientEmail] = "assets/taxi.jpg";
            else
              this.profilePictures[c.clientEmail] = `data:image/jpeg;base64,${encodedImage.data}`;
          });
      }
    }
  }

  selectChat(email: string) {
    // console.log("selectChat")
    for (const c of this.chats) {
      if (c.clientEmail === email) {
        this.onChatSelection.emit(c);
        console.log("emit(chat) for " + email)
      }
    }
  }

}
