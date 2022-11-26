import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";
import {Comment} from "../../../../model/Review";

@Component({
  selector: 'app-admin-comments',
  templateUrl: './admin-comments.component.html',
  styleUrls: ['./admin-comments.component.css']
})
export class AdminCommentsComponent implements OnInit {
  commentSectionVisible: boolean = false;
  commentFormControl = new FormControl('', [Validators.required]);
  comments: Comment[] = [];

  constructor() { }

  ngOnInit(): void {
  }

  leaveAComment() {
    // TODO
  }
}
