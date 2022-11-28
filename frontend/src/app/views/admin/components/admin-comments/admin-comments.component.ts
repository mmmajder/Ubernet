import {Component, Input, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Comment} from "../../../../model/Review";
import {CommentsService} from "../../../../services/comments.service";
import {formatTime} from "../../../../services/utils.service";
import {Store} from "@ngxs/store";

@Component({
  selector: 'app-admin-comments',
  templateUrl: './admin-comments.component.html',
  styleUrls: ['./admin-comments.component.css']
})
export class AdminCommentsComponent implements OnInit {
  commentSectionVisible: boolean = false;
  commentFormControl = new FormControl('', [Validators.required]);
  comments: Comment[] = [];
  content: string = "";
  adminEmail: string;
  @Input() userEmail: string;

  constructor(private commentsService: CommentsService, private store: Store) {
  }

  ngOnInit(): void {
    this.loadComments();
    this.store.select(state => state.loggedUser).subscribe({
      next: (user) => {
        this.adminEmail = user.email;
      }
    })
  }

  leaveAComment() {
    this.commentsService.addComments(this.userEmail, this.adminEmail, this.content).subscribe({
      next: () => {
        this.loadComments();
        this.content = "";
      }
    })
  }

  blockThisUser() {
    // TODO
  }

  formatTime(time: number[]): string {
    return formatTime(time);
  }

  private loadComments() {
    this.commentsService.getComments(this.userEmail).subscribe({
      next: value => {this.comments = value; console.log(value)}
    })
  }
}
