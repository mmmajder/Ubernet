import {Component, OnInit} from '@angular/core';
import {Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {Output, EventEmitter, Input} from '@angular/core';
import {CarTypeService} from "../../../../services/car-type.service";
import {PositionDTO} from "../../../../model/PositionDTO";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-search-directions-customer',
  templateUrl: './search-directions-customer.component.html',
  styleUrls: ['./search-directions-customer.component.css']
})
export class SearchDirectionsCustomerComponent implements OnInit {
  positions: (Position | null)[];
  @Input() estimations: MapSearchEstimations
  @Output() addPinsToMap = new EventEmitter<Position[]>();
  @Output() getSelectedCarType = new EventEmitter<string>();
  // carType: string;
  carTypes: string[];
  canOptimize: boolean = true;
  friends: ({ friendEmail: string })[];
  hasPet: boolean;
  hasChild: boolean;

  firstFormGroup: FormGroup;
  destinationsForm: any;
  carTypeFormGroup: any;
  secondFormGroup: FormGroup;
  friendsFormGroup: FormGroup;



  constructor(private mapService: MapService, private carTypeService: CarTypeService, private _snackBar: MatSnackBar, private _formBuilder: FormBuilder) {
    this.friends = [
      // {friendEmail: ""}
    ]
    this.hasChild = false;
    this.hasPet = false;
  }

  get destinations() {
    return (<FormArray>this.destinationsForm.get('destinations'))
  }

  get carType() {
    return (<FormArray>this.carTypeFormGroup.get('carType'))
  }

  get newFriend() {
    return (<FormControl>this.friendsFormGroup.get('newFriend'))
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
    this.destinationsForm = new FormGroup({
      destinations: new FormArray([
        new FormControl("", Validators.required),
        new FormControl("", Validators.required)
      ])
    })
    this.carTypeFormGroup = new FormGroup({
      carType: new FormControl("", Validators.required)
    })
    this.firstFormGroup = new FormGroup({groups: this.destinationsForm});
    this.secondFormGroup = new FormGroup({groups: this.carTypeFormGroup});
    this.friendsFormGroup = new FormGroup({
        newFriend: new FormControl("", [Validators.required, Validators.email])
    })

  }

  addNewDestination() {
    this.destinations.push(new FormControl("", Validators.required))
  }

  removeDestination(number: number) {
    this.destinations.removeAt(number)
  }

  clearInputFields() {
    this.destinations.controls.forEach((destination) => {
      destination.setValue(null)
    })
    console.log(this.destinations.value)
  }

  async showEstimates() {
    const validInput = () => {
      let isValid = true
      this.destinations.controls.forEach((destination) => {
        if (destination.value=="") {
          isValid = false
        }
      })
      return isValid
    }

    if (!validInput()) {
      return
    }
    console.log(this.destinations)
    this.positions = [];
    await this.calculatePositionsSearch()
    const validOutput = () => {
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

    if (validOutput()) {
      this.addPinsToMap.emit(castToPositions(this.positions))
    } else {
      this._snackBar.open("Please enter all existing locations!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    }
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      for (let i = 0; i < this.destinations.value.length; i++) {
        let destination = this.destinations.value[i]
        this.mapService.findAddress(destination).subscribe((response: Object) => {
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
  }

  changeCarType(event: string) {
    console.log(event)
    this.getSelectedCarType.emit(this.carType.value)
  }


  optimizeByPrice() {

  }

  reserveRide() {

  }

  requestRide() {

  }

  removeFriend(i: number) {
    this.friends = this.friends.filter(function (elem, index) {
      return index != i;
    });
  }

  addFriend() {
    if (this.newFriend.invalid) {
      return
    }
    this.friends.push({friendEmail: this.newFriend.value})
    this.newFriend.reset()
  }
}
