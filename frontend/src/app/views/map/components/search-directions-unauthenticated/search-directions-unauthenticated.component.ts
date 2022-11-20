import { Component, OnInit } from '@angular/core';
import {FormBuilder} from "@angular/forms";
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {SearchDestinationElement} from "../../../../model/SearchDestinationElement";

@Component({
  selector: 'app-search-directions-unauthorised',
  templateUrl: './search-directions-unauthenticated.component.html',
  styleUrls: ['./search-directions-unauthenticated.component.css']
})
export class SearchDirectionsUnauthenticatedComponent implements OnInit {
  destinations: SearchDestinationElement[];
  estimatesDisplayed: boolean
  positions: Position[]

  constructor(private mapService: MapService) {
    this.destinations = [
      {locationName: ""},
      {locationName: ""}
    ]
    this.estimatesDisplayed = false
  }

  ngOnInit(): void {
  }

  addNewDestination() {
    this.destinations.push({locationName: ""})
  }

  removeDestination() {
  }

  showEstimates() {
    this.estimatesDisplayed = true
  }
}
