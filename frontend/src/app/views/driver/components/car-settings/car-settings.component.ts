import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-car-settings',
  templateUrl: './car-settings.component.html',
  styleUrls: ['./car-settings.component.css']
})
export class CarSettingsComponent implements OnInit {
  nameFormControl = new FormControl('', [Validators.required]);
  name: string = "";
  price: number = 0;
  allowsBabies: boolean = false;
  allowsPets: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  saveChanges() {

  }
}
