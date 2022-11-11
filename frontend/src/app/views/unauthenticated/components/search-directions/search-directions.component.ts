import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormArray, Validators} from '@angular/forms';

@Component({
  selector: 'app-search-directions',
  templateUrl: './search-directions.component.html',
  styleUrls: ['./search-directions.component.css']
})
export class SearchDirectionsComponent implements OnInit {
  public destinations: ({ number: Number; locationName: string })[];

  constructor(private formBuilder: FormBuilder) {
    this.destinations = [
      {number: 1, locationName: ""},
      {number: 2, locationName: ""}
    ]
  }

  ngOnInit(): void {
  }

  addNewDestination() {
    this.destinations.push({number: this.destinations.length + 1, locationName: ""})
  }

  removeDestination() {
    console.log("AAAAAAAAAAAA")
    // this.destinations.pop({number: this.destinations.length + 1, locationName: ""})
  }


}
