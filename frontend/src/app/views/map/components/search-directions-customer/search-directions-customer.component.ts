import {Component, OnInit} from '@angular/core';
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {Output, EventEmitter, Input} from '@angular/core';


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
  @Output() addPinsToMap = new EventEmitter<Position[]>();

  constructor(private mapService: MapService) {
    this.destinations = [
      {locationName: ""},
      {locationName: ""}];
    this.estimatesPresented = false;
  }

  ngOnInit(): void {
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
    this.addPinsToMap.emit(this.positions)
    this.estimatesPresented = true;
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      this.destinations.forEach((destination) => {
        this.mapService.findAddress(destination.locationName).subscribe((response) => {
          let position = new Position()
          position.x = Object.values(response)[0].lon
          position.y = Object.values(response)[0].lat
          this.positions.push(position)
        })
      })
      setTimeout(() => {
        resolve('resolved');
      }, 1000);
    });
  }


  setDestination(text: string, i: number) {
    this.destinations[i].locationName = text
  }
}