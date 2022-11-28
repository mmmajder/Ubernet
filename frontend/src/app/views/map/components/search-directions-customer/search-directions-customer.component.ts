import {Component, OnInit} from '@angular/core';
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {Output, EventEmitter, Input} from '@angular/core';
import {CarTypeService} from "../../../../services/car-type.service";
import {PositionDTO} from "../../../../model/PositionDTO";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-search-directions-customer',
  templateUrl: './search-directions-customer.component.html',
  styleUrls: ['./search-directions-customer.component.css']
})
export class SearchDirectionsCustomerComponent implements OnInit {
  estimatesPresented: boolean;
  public destinations: ({ locationName: string })[];
  positions: (Position | null)[];
  @Input() estimatedTime: string;
  @Input() estimatedPrice: number
  @Output() addPinsToMap = new EventEmitter<Position[]>();
  @Output() getSelectedCarType = new EventEmitter<string>();
  carType: string;
  carTypes: string[];
  canOptimize: boolean = true;

  constructor(private mapService: MapService, private carTypeService: CarTypeService, private _snackBar: MatSnackBar) {
    this.destinations = [
      {locationName: ""},
      {locationName: ""}];
    this.carType = 'Default';
    this.estimatesPresented = true;
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

    const validInput = () => {
      if (this.carType=="Default") {
        return false
      }
      let isValid = true
      console.log("Pos")
      console.log(this.positions)
      this.positions.forEach((position) => {
        console.log("position")
        console.log(position)
        if (position == undefined) {
          isValid = false;
        }
      })
      return isValid;
    }

    const castToPositions = (positions: (Position | null)[]) => {
      let retPositions: Position[] = []
      positions.forEach((position) => {
        if (position != null) {
          retPositions.push(position)
        }
      })
      return retPositions
    }

    if (validInput()) {
      this.getSelectedCarType.emit(this.carType)
      this.addPinsToMap.emit(castToPositions(this.positions))
      this.estimatesPresented = true;
    } else {
      this._snackBar.open("Please enter all existing locations!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    }
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      for (let i = 0; i < this.destinations.length; i++) {
        let destination = this.destinations[i]
        this.mapService.findAddress(destination.locationName).subscribe((response: Object) => {
          let positions: PositionDTO[] = Object.values(response)
          if (positions.length == 0) {
            this.positions[i] = null
          } else {
            let position = new Position()
            console.log(response)
            position.x = Object.values(response)[0].lon
            position.y = Object.values(response)[0].lat
            this.positions[i] = position
          }
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

  optimizeByPrice() {

  }

  reserveRide() {

  }

  requestRide() {

  }
}
