import {Component, OnInit} from '@angular/core';
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {Output, EventEmitter, Input} from '@angular/core';
import {ErrorStateMatcher} from "@angular/material/core";
import {CarTypeService} from "../../../../services/car-type.service";

@Component({
  selector: 'app-search-directions-customer',
  templateUrl: './search-directions-customer.component.html',
  styleUrls: ['./search-directions-customer.component.css']
})
export class SearchDirectionsCustomerComponent implements OnInit {
  estimatesPresented: boolean;
  public destinations: ({ locationName: string })[];
  positions: Position[];
  @Input()
  estimatedTime: string;
  @Input()
  estimatedPrice: number
  @Output() addPinsToMap = new EventEmitter<Position[]>();
  @Output() getSelectedCarType = new EventEmitter<string>();
  carType: string;
  carTypes: string[];

  constructor(private mapService: MapService, private carTypeService: CarTypeService) {
    this.destinations = [
      {locationName: ""},
      {locationName: ""}];
    this.carType = 'Default';
    this.estimatesPresented = false;
  }

  ngOnInit(): void {
    this.carTypeService.getCarTypes().subscribe({
      next: (carTypeGetResponse) => {
        this.carTypes = []
        carTypeGetResponse.forEach((type) => {
          this.carTypes.push(type.name);
        })
      },
    });
  }

  addNewDestination() {
    this.destinations.push({locationName: ""});
  }

  removeDestination(number: number) {
    this.destinations = this.destinations.filter(function (elem, index) {
      return index != number;
    });

  }

  async showEstimates() {
    this.positions = [];
    await this.calculatePositionsSearch()
    this.getSelectedCarType.emit(this.carType)
    this.addPinsToMap.emit(this.positions)
    this.estimatesPresented = true;
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      for (let i = 0; i < this.destinations.length; i++) {
        let destination = this.destinations[i]
        this.mapService.findAddress(destination.locationName).subscribe((response) => {
          let position = new Position()
          position.x = Object.values(response)[0].lon
          position.y = Object.values(response)[0].lat
          this.positions[i] = position
        })
      }
      setTimeout(() => {
        resolve('resolved');
      }, 3000);
    });
  }


  setDestination(text: string, i: number) {
    this.destinations[i].locationName = text
  }

  changeCarType(event: string) {
    console.log(event)
    this.carType = event
  }

  clearInputFields() {
    this.carType = 'Default';
    this.destinations.forEach((destination) => {
      destination.locationName = ""
    })
  }
}
