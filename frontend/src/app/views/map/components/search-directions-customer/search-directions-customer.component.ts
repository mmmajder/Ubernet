import {Component, OnInit} from '@angular/core';
import {Place, Position} from "../../../../model/Position";
import {MapService} from "../../../../services/map.service";
import {Output, EventEmitter, Input} from '@angular/core';
import {CarTypeService} from "../../../../services/car-type.service";
import {PositionDTO} from "../../../../model/PositionDTO";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {RideCreate} from "../../../../model/RideCreate";
import {RideService} from "../../../../services/ride.service";
import {FriendEmailDTO} from "../../../../model/FriendEmailDTO";
import {Customer, User} from "../../../../model/User";
import {PaymentDTO} from "../../../../model/PaymentDTO";
import {DecrementTokens} from "../../../../store/actions/tokens.action";
import {RideDetails} from "../../../../model/RideDetails";
import {Store} from "@ngxs/store";
import {LeafletRoute} from "../../../../model/LeafletRoute";
import {CustomersService} from "../../../../services/customers.service";
import {RideAlternativeService} from "../../../../services/ride-alternative.service";
import {RideDTO} from "../../../../model/RideDTO";
import {OpenStreetMapProvider} from 'leaflet-geosearch';
import {debounceTime, distinctUntilChanged, from, Observable, startWith, switchMap} from "rxjs";
import {SearchEstimation} from "../../../../model/SearchEstimation";

@Component({
  selector: 'app-search-directions-customer',
  templateUrl: './search-directions-customer.component.html',
  styleUrls: ['./search-directions-customer.component.css']
})
export class SearchDirectionsCustomerComponent implements OnInit {
  positions: (Place | null)[];
  @Input() estimations: SearchEstimation
  @Input() selectedRoute: LeafletRoute[]
  @Input() loggedUser: User
  @Input() allAlternatives: LeafletRoute[][];
  @Input() favoriteRide: RideDTO;
  @Output() addPinsToMap = new EventEmitter<Place[]>();
  @Output() getSelectedCarType = new EventEmitter<string>();
  @Output() optimizeByPrice = new EventEmitter()
  @Output() optimizeByTime = new EventEmitter()
  carTypes: string[];
  friends: ({ friendEmail: string })[];
  hasPet: boolean;
  hasChild: boolean;

  firstFormGroup: FormGroup;
  destinationsForm: any;
  carTypeFormGroup: any;
  secondFormGroup: FormGroup;
  friendsFormGroup: FormGroup;
  searching = false;
  findingDriver = false;
  timeOfRide: string
  typeOfRequest: string;
  isActive: boolean;
  provider = new OpenStreetMapProvider();
  filteredOptions: Observable<string[]>[]

  constructor(private rideAlternativeService: RideAlternativeService, private customerService: CustomersService, private store: Store, private mapService: MapService, private rideService: RideService, private carTypeService: CarTypeService, private _snackBar: MatSnackBar, private _formBuilder: FormBuilder) {
    this.friends = []
    this.typeOfRequest = "now"
    this.hasChild = false;
    this.hasPet = false;
    this.timeOfRide = new Date().toLocaleString('en-US', {hour: 'numeric', minute: 'numeric', hour12: true})
    this.filteredOptions = []
    this.destinationsForm = new FormGroup({
      destinations: new FormArray([
        new FormControl("", Validators.required),
        new FormControl("", Validators.required)
      ]),
    })
    this.ManageNameControl(0)
    this.ManageNameControl(1)
  }

  ManageNameControl(index: number) {
    const arrayControl = this.destinationsForm.get('destinations') as FormArray;
    this.filteredOptions[index] = arrayControl.controls[index].valueChanges
      .pipe(
        startWith(''),
        debounceTime(400),
        distinctUntilChanged(),
        switchMap(name => {
          return this._filter(name || '')
        })
      )
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
    this.customerService.getById(this.loggedUser.email).subscribe((customer: Customer) => {
      this.isActive = customer.isActive
    })
    this.carTypeService.getCarTypes().subscribe({
      next: (carTypeGetResponse) => {
        this.carTypes = []
        carTypeGetResponse.forEach((type) => {
          this.carTypes.push(type.name);
        })
      },
    });

    this.carTypeFormGroup = new FormGroup({
      carType: new FormControl("", Validators.required)
    })

    this.firstFormGroup = new FormGroup({groups: this.destinationsForm});
    this.secondFormGroup = new FormGroup({groups: this.carTypeFormGroup});
    this.friendsFormGroup = new FormGroup({
      newFriend: new FormControl("", [Validators.required, Validators.email])
    })
    if (this.favoriteRide !== undefined) {
      this.initFavRoute();
    }
  }

  initFavRoute() {
    this.positions = this.favoriteRide.route.checkPoints
    for (let i = 0; i < this.favoriteRide.route.checkPoints.length; i++) {
      const checkpoint = this.favoriteRide.route.checkPoints[i];
      if (i < 2)
        this.destinations.controls[i].setValue(checkpoint.name)
      else {
        this.destinations.push(new FormControl(checkpoint.name, Validators.required))
      }
    }
    this.showEstimates().then()
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
  }

  async showEstimates() {
    this.searching = true;
    if (!validInput(this.destinations.controls)) {
      this._snackBar.open("Please enter existing locations!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
      return
    }
    this.positions = [];
    await this.calculatePositionsSearch()

    if (validOutput(this.positions)) {
      this.addPinsToMap.emit(castToPlace(this.positions))
      this.searching = false;
    } else {
      this._snackBar.open("Please enter existing locations!", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    }

    function validInput(destinations: any[]) {
      let isValid = true
      destinations.forEach((destination) => {
        if (destination.value == "") {
          isValid = false
        }
      })
      return isValid
    }

    function validOutput(positions: (Place | null)[]) {
      let isValid = true
      positions.forEach((position) => {
        if (position == undefined) {
          isValid = false;
        }
      })
      return isValid;
    }

    function castToPlace(positions: (Place | null)[]) {
      const retPositions: Place[] = []
      positions.forEach((position) => {
        if (position != null) {
          retPositions.push(position)
        }
      })
      return retPositions
    }
  }

  calculatePositionsSearch() {
    return new Promise(resolve => {
      for (let i = 0; i < this.destinations.value.length; i++) {
        const destination = this.destinations.value[i]
        this.mapService.findAddress(destination).subscribe((response: Object) => {
          const positions: PositionDTO[] = Object.values(response)
          if (positions.length == 0) {
            this.positions[i] = null
          } else {
            const position = new Position()
            position.x = Object.values(response)[0].lon
            position.y = Object.values(response)[0].lat
            this.positions[i] = {
              "position": position,
              "name": destination
            }
          }
        })
      }
      setTimeout(() => {
        resolve('resolved');
      }, 3000);
    });
  }

  changeCarType() {
    this.getSelectedCarType.emit(this.carType.value)
  }


  optimizePrice() {
    this.optimizeByPrice.emit()
  }

  optimizeTime() {
    this.optimizeByTime.emit()
  }

  mergeRoutePaths() {
    const mergedRoute = new RideCreate();
    this.selectedRoute.forEach((partOfRoute: LeafletRoute, index: number) => {
      if (index != 0) {
        partOfRoute.coordinates.shift()
      }
      mergedRoute.coordinates = mergedRoute.coordinates.concat(partOfRoute.coordinates)
      mergedRoute.instructions = mergedRoute.instructions.concat(partOfRoute.instructions)
      mergedRoute.totalDistance = mergedRoute.totalDistance + partOfRoute.summary.totalDistance
      mergedRoute.totalTime = mergedRoute.totalTime + partOfRoute.summary.totalTime
      mergedRoute.numberOfRoute[index] = partOfRoute.routesIndex
    })
    return mergedRoute;
  }

  createRide(route: RideCreate, payment: PaymentDTO): RideCreate {
    const ride = new RideCreate()
    ride.coordinates = route.coordinates
    ride.instructions = route.instructions
    ride.carType = this.carType.value
    ride.hasPet = this.hasPet
    ride.hasChild = this.hasChild
    ride.totalDistance = route.totalDistance
    ride.totalTime = route.totalTime
    ride.reservationTime = this.timeOfRide
    ride.route = this.positions
    ride.numberOfRoute = route.numberOfRoute
    ride.payment = payment
    ride.passengers = [this.loggedUser.email]
    ride.reservation = this.typeOfRequest === "reserve"
    return ride
  }

  requestRide() {
    this.findingDriver = true;
    const payment = new PaymentDTO();
    payment.customerThatPayed = this.loggedUser.email
    payment.totalPrice = +this.estimations.price
    const route = this.mergeRoutePaths()
    const ride = this.createRide(route, payment)
    this.friends.forEach((friend: FriendEmailDTO) => {
      ride.passengers.push(friend.friendEmail)
    })

    this.rideService.createRideRequest(ride).subscribe({
      next: (res: RideDetails) => {
        this.store.dispatch([new DecrementTokens(res.payment.customers[0].pricePerCustomer)])
        this.findingDriver = false;
        this._snackBar.open("Successfully reserved ride", '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
        this.createRideAlternatives(res.id)
      },
      error: (res: any) => {
        this.findingDriver = false;
        this._snackBar.open(res.error, '', {
          duration: 3000,
          panelClass: ['snack-bar']
        })
      }
    })
  }

  createRideAlternatives(rideId: number) {
    this.rideAlternativeService.createRideAlternatives(rideId, this.allAlternatives).subscribe(() => {
    })
  }

  removeFriend(i: number) {
    this.friends = this.friends.filter(function (elem, index) {
      return index != i;
    });
  }

  addFriend() {
    if(this.loggedUser.email === this.newFriend.value) {
      this._snackBar.open("You cannot add yourself.", '', {
        duration: 3000,
        panelClass: ['snack-bar']
      })
    } else if (this.newFriend.valid) {
      this.friends.push({friendEmail: this.newFriend.value})
      this.newFriend.reset()
    }
  }

  private _filter(value: string): Observable<string[]> {
    const getSuggestions = async () => {
      const suggestions = await this.provider.search({
        query: value
      });
      return suggestions.map(i => i.label).slice(0, 3)
    }
    return from(getSuggestions())
  }
}
