import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  hide: boolean = true;
  hide1: boolean = true;
  hide2: boolean = true;

  password: any;
  password1: any;
  password2: any;

  passwordFormControl = new FormControl('', [Validators.required]);
  password1FormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  password2FormControl = new FormControl('', [Validators.required]);

  constructor() {
  }

  ngOnInit(): void {
  }

}
