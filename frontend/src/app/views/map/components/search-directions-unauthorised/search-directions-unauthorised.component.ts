import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-search-directions-unauthorised',
  templateUrl: './search-directions-unauthorised.component.html',
  styleUrls: ['./search-directions-unauthorised.component.css']
})
export class SearchDirectionsUnauthorisedComponent implements OnInit {
  destinations: ({ number: Number; locationName: string })[];
  estimatesDisplayed: boolean

  constructor(private formBuilder: FormBuilder) {
    this.destinations = [
      {number: 0, locationName: ""},
      {number: 1, locationName: ""}
    ]
    this.estimatesDisplayed = false
  }

  ngOnInit(): void {
  }

  addNewDestination() {
    this.destinations.push({number: this.destinations.length, locationName: ""})
  }

  removeDestination() {
    console.log("AAAAAAAAAAAA")
    // this.destinations.pop({number: this.destinations.length + 1, locationName: ""})
  }

  showEstimates() {
    this.estimatesDisplayed = true
  }
}
