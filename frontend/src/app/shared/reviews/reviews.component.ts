import { Component, OnInit } from '@angular/core';
import {Review} from "../../model/Review";
import {ImageService} from "../../services/image.service";

@Component({
  selector: 'app-reviews',
  templateUrl: './reviews.component.html',
  styleUrls: ['./reviews.component.css']
})
export class ReviewsComponent implements OnInit {
  reviews: Review[] = [];
  profilePictures: Map<string, string> = new Map<string, string>();

  constructor(private imageService: ImageService) { }

  ngOnInit(): void {
    this.loadProfileImages();
  }

  private loadProfileImages() {
    for (let i = 0; i < this.reviews.length; i++) {
      this.imageService.getProfileImage(this.reviews[i].reviewer.email)
        .subscribe((encodedImage: any) => {
          if (encodedImage === null)
            this.profilePictures.set(this.reviews[i].reviewer.email, "../../../../assets/taxi.jpg");
          else
            this.profilePictures.set(this.reviews[i].reviewer.email, `data:image/jpeg;base64,${encodedImage.data}`);
        });
    }
  }

}
