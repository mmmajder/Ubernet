import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {CarTypeService} from "../../../../services/car-type.service";

@Component({
  selector: 'app-search-directions-unauthorised',
  templateUrl: './search-directions-unauthenticated.component.html',
  styleUrls: ['./search-directions-unauthenticated.component.css']
})
export class SearchDirectionsUnauthenticatedComponent implements OnInit {
  estimatesDisplayed: boolean
  positions: Position[]
  fromValue: string;
  toValue: string;
  carType: string;
  carTypes: string[];

  @Input()
  estimatedTime: string;
  @Input()
  estimatedPrice: string
  @Output() addPinsToMap = new EventEmitter<Position[]>();
  @Output() getSelectedCarType = new EventEmitter<string>();

  constructor(private mapService: MapService, private carTypeService: CarTypeService) {
    this.fromValue = ""
    this.toValue = ""
    this.estimatesDisplayed = false
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

  async showEstimates() {
    this.positions = [];
    await this.calculatePositionsSearch()
    this.getSelectedCarType.emit(this.carType)
    this.addPinsToMap.emit(this.positions)
    this.estimatesDisplayed = true;
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      let destinations = [this.fromValue, this.toValue]
      for (let i = 0; i < destinations.length; i++) {
        let destination = destinations[i]
        this.mapService.findAddress(destination).subscribe((response) => {
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

  changeCarType(event: string) {
    console.log(event)
    this.carType = event
  }

  clearInputFields() {
    this.carType = 'Default';
    this.fromValue = ""
    this.toValue = ""
  }
}
