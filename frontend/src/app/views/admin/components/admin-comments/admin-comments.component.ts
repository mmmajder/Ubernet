import {Component, Input, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Comment} from "../../../../model/Review";
import {CommentsService} from "../../../../services/comments.service";

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
  @Input() userEmail: string;

  constructor(private commentsService: CommentsService) {
  }

  ngOnInit(): void {
    this.commentsService.getComments(this.userEmail).subscribe({
      next: value => {this.comments = value; console.log(value)}
    })
  }

  leaveAComment() {
    // this.commentsService.getComments(this.userEmail).subscribe({
    //   next: value => this.comments = value
    // })
  }

  blockThisUser() {
    // TODO
  }
}
