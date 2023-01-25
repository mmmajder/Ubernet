import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Place, Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {CarTypeService} from "../../../../services/car-type.service";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";

@Component({
  selector: 'app-search-directions-unauthorised',
  templateUrl: './search-directions-unauthenticated.component.html',
  styleUrls: ['./search-directions-unauthenticated.component.css']
})
export class SearchDirectionsUnauthenticatedComponent implements OnInit {
  estimatesDisplayed: boolean
  positions: Place[]
  fromValue: string;
  toValue: string;
  carType: string;
  carTypes: string[];

  // @Input()
  // estimatedTime: string;
  // @Input()
  // estimatedPrice: string
  @Input() estimations: MapSearchEstimations
  @Output() addPinsToMap = new EventEmitter<Place[]>();
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
      const destinations = [this.fromValue, this.toValue]
      for (let i = 0; i < destinations.length; i++) {
        const destination = destinations[i]
        this.mapService.findAddress(destination).subscribe((response) => {
          const position = new Position()
          position.x = Object.values(response)[0].lon
          position.y = Object.values(response)[0].lat
          this.positions[i] = {
            "position": position,
            "name": destination
          }
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
