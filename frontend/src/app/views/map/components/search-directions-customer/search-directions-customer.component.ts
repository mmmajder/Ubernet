import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormArray, Validators} from '@angular/forms';

@Component({
  selector: 'app-search-directions-customer',
  templateUrl: './search-directions-customer.component.html',
  styleUrls: ['./search-directions-customer.component.css']
})
export class SearchDirectionsCustomerComponent implements OnInit {
  estimatesPresented: boolean
  public destinations: ({ destination: string })[];

  constructor(private formBuilder: FormBuilder) {
    this.destinations = [
      {destination: ""},
      {destination: ""}]
    this.estimatesPresented = false
  }

  ngOnInit(): void {
  }

  addNewDestination() {
    this.destinations.push({destination: ""})
  }

  removeDestination(number: number) {
    console.log(this.destinations)
    console.log("number " + number)
    this.destinations = this.destinations.filter(function (elem, index) {
      return index != number;
    });

  }

  showEstimates() {
    this.estimatesPresented = true
  }

  setDestination(text: string, i: number) {
    console.log(text)
    console.log(i)
    console.log(this.destinations)
  }
}
