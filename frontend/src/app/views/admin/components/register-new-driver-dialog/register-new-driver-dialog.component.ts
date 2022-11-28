import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, Validators} from "@angular/forms";
import {map, Observable} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";

@Component({
  selector: 'app-register-new-driver-dialog',
  templateUrl: './register-new-driver-dialog.component.html',
  styleUrls: ['./register-new-driver-dialog.component.css']
})
export class RegisterNewDriverDialogComponent implements OnInit {

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  phoneFormControl = new FormControl('', [Validators.required]);
  nameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  cityFormControl = new FormControl('', [Validators.required]);
  passwordFormControl = new FormControl('', [Validators.required, Validators.minLength(6)]);
  password2FormControl = new FormControl('', [Validators.required]);

  firstFormGroup = this._formBuilder.group({
    emailFormCtr: ['', Validators.required, Validators.email],
    phoneFormCtr: ['', Validators.required],
    nameFormCtr: ['', Validators.required],
    lastNameFormCtr: ['', Validators.required],
    cityFormCtr: ['', Validators.required],
    passwordFormCtr: ['', Validators.required, Validators.minLength(6)],
    password2FormCtr: ['', Validators.required, Validators.minLength(6)]
  });
  secondFormGroup = this._formBuilder.group({
    secondCtrl: ['', Validators.required],
  });
  thirdFormGroup = this._formBuilder.group({
    thirdCtrl: ['', Validators.required],
  });
  stepperOrientation: Observable<StepperOrientation>;

  email: string = "";
  phoneNumber: string = "";
  password: string = "";
  password2: string = "";
  name: string = "";
  lastName: string = "";
  city: string = "";

  hide: boolean = true;
  hide2: boolean = true;

  constructor(private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  ngOnInit(): void {
  }

}
