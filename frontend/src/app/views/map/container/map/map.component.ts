import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, Marker} from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";
import {UserRole} from "../../../../model/UserRole";
import {ActiveCarResponse} from "../../../../model/ActiveCarResponse";
import {Place, Position} from "../../../../model/Position";
import {secondsToDhms} from "../../../../services/utils.service";
import {RidePayService} from "../../../../services/ride-price.service";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {RideService} from "../../../../services/ride.service";
import {User, userIsDriver} from "../../../../model/User";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {Router} from "@angular/router";
import {Store} from "@ngxs/store";
import {CarService} from "../../../../services/car.service";
import {PositionInTime} from "../../../../model/PositionInTime";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit, OnInit {
  userRole: UserRole | null
  userRoles = UserRole
  map: L.Map
  searchPins: Marker[]
  totalTime: number
  estimationsSearch: MapSearchEstimations
  typeOfVehicle: string
  searchedRoutes: any;
  directionRoutes: Map<number, L.Control>
  private stompClient: any;
  pins: Marker[];
  selectedRoute: any;
  loggedUser: User;

  constructor(private mapService: MapService, private ridePayService: RidePayService, private rideService: RideService, private router: Router, private store: Store, private carService: CarService) {
    this.searchPins = [];
    this.totalTime = 0;
    this.searchedRoutes = [];
    this.estimationsSearch = new MapSearchEstimations();
    this.pins = []
    this.directionRoutes = new Map<number, L.Control>;
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.loggedUser = resp.loggedUser;
        if (resp.loggedUser.role === "CUSTOMER") {
          this.userRole = UserRole.CUSTOMER
        }
        this.initMap()
        this.initializeWebSocketConnection();
        this.initPins()
      },
      error: () => this.router.navigate(['/'])
    });
  }

  reinitializeDirectionsForDrivers() {
    for (let [key, value] of this.directionRoutes) {
      this.map.removeControl(value)
    }
  }

  initializeWebSocketConnection() {
    let ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/map-updates/update-vehicle-position", (message: any) => {
      this.reinitializeDirectionsForDrivers();
      this.removePins()
      this.initPins();
    })
    this.stompClient.subscribe("/map-updates/update-route-for-selected-car", (message: any) => {
      this.createRouteForSelectedCar(JSON.parse(message.body))
    })
  }

  createRouteForSelectedCar(ride: any) {
    let checkPoints: LatLng[] = []
    let start: Position
    if (ride.driver.car.futureRide === null)
      start = ride.driver.car.position
    else
      start = this.rideService.getLastPosition(ride.driver.car.futureRide.positions)
    let end = ride.driver.car.currentRide.positions[0].position
    checkPoints.push(L.latLng(start.y, start.x))
    checkPoints.push(L.latLng(end.y, end.x))
    let route = L.Routing.control({
      waypoints: checkPoints,
      altLineOptions: {
        extendToWaypoints: false,
        missingRouteTolerance: 0,
      },
    }).on('routesfound', (response) => {
      let route = response.routes[0]
      console.log("OOOOOOOOOOO")
      console.log(route)
      this.rideService.createRouteForSelectedCar(route, ride.driver.car.id).subscribe(() => {
        console.log("STIGAO sam")
      })
    }).addTo(this.map)

  }

  private initMap() {
    this.map = L.map('map').setView([45.267136, 19.833549], 11);
    let mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(this.map);
  }

  removePins() {
    if (this.pins != [])
      this.pins.forEach((marker: Marker) => {
        this.map.removeLayer(marker)
      })
  }


  initPins() {
    this.mapService.getActiveCars().subscribe((activeCars) => {
      if (userIsDriver(this.loggedUser)) {
        let car = this.loggedUser.car
        console.log(car)
        this.carService.getActiveCar(this.loggedUser.email).subscribe((car: ActiveCarResponse) => {
          console.log(car)
          let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
          this.pins.push(marker)
          if (car.currentRide != null)
            this.initDirections(car, marker)
        })
      } else {
        activeCars.forEach((car: ActiveCarResponse) => {
          let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
          this.pins.push(marker);
        })
      }
    })

  }

  initDirections = (car: ActiveCarResponse, marker: Marker<any>) => {
    let waypoints = []
    console.log(car)
    let positionsInTime: PositionInTime[] = car.currentRide.positions
    let last = positionsInTime.length - 1
    waypoints.push(L.latLng(positionsInTime[0].position.y, positionsInTime[0].position.x))
    waypoints.push(L.latLng(positionsInTime[last].position.y, positionsInTime[last].position.x))

    let plan = L.Routing.plan(waypoints, {
      createMarker: (i, start, n) => {
        let marker_icon;
        if (i == 0) {
          // This is the first marker, indicating start
          marker_icon = this.greenIcon
        } else if (i == n - 1) {
          //This is the last marker indicating destination
          marker_icon = this.redIcon
        }
        return L.marker(start.latLng, {
          icon: marker_icon
        })
      }
    })
    let route = L.Routing.control({
      waypoints: waypoints,
      routeWhileDragging: false,
      addWaypoints: false,
      fitSelectedRoutes: false,
      // plan: plan
    }).on('routesfound', (response) => {
      let timeSlots = this.mapService.getTimeSlots(response);   // get time when reaching new location
      this.mapService.saveFoundPositionsOfRide(response.routes[0].coordinates, timeSlots)
      while (timeSlots.length < response.routes[0].coordinates.length) {
        timeSlots.push(timeSlots[timeSlots.length - 1])
      }
    }).addTo(this.map)
    route.on('add', (e) => {
      var waypoints = route.getWaypoints();
      var markers = e.target._markers;
      for (var i = 0; i < markers.length; i++) {
        if (i === 0) {
          markers[i].setIcon(this.redIcon);
        } else if (i === markers.length - 1) {
          markers[i].setIcon(this.redIcon);
        }
      }
    })
    this.directionRoutes.set(car.carId, route)
  }


// setNewDestination(car: ActiveCarResponse, marker: Marker<any>): ActiveCarResponse {
//   console.log(car.carId)
//   let returnData = car;
//   this.mapService.setNewDestinationOfCar(car.carId).subscribe(() => {
//     let getNewDestination = () => {
//       this.mapService.getCarById(car.carId).subscribe((newCarData) => {
//         if (newCarData.currentPosition.id === newCarData.destinations[0].id) {
//           getNewDestination()
//         } else {
//           this.initDirections(newCarData, marker)
//         }
//       })
//     }
//     getNewDestination()
//   })
//   return returnData;
// }

  removeRoutingControls = (routingControls: any) => {
    if (routingControls == undefined) {
      return
    }
    routingControls.forEach((routingControl: any) => {
      this.map.removeControl(routingControl);
      routingControl = null;
    })
  }

  drawSearchedRoute(positions: Place[]) {
    this.removeRoutingControls(this.searchedRoutes)

    let checkPoints: LatLng[] = []
    for (let i = 0; i < positions.length; i++) {
      checkPoints.push(L.latLng(positions[i].position.y, positions[i].position.x))
    }

    const drawRoutes = () => {
      let route = L.Routing.control({
        waypoints: checkPoints,
        altLineOptions: {
          extendToWaypoints: false,
          missingRouteTolerance: 0,
          styles: [
            {color: 'blue', opacity: 0.5}
          ],
        },

        routeWhileDragging: true,
        addWaypoints: false,
        showAlternatives: true,
        useZoomParameter: false,

        // geocoder: L.control.Geocode.nominatim()

      }).on('routesfound', (response) => {
        let route = response.routes[0]
        this.totalTime = route.summary.totalTime
        this.estimationsSearch.time = secondsToDhms(this.totalTime)
        this.estimationsSearch.lengthInKm = route.summary.totalDistance / 1000
        if (this.typeOfVehicle != undefined) {
          this.calculatePrice()
        }
      })
        .on('routeselected', (e) => {
          let route = e.route
          console.log("IIIIIIIIIIIIIIIIIIIIIIIIIIIIII")
          console.log(route)
          this.selectedRoute = e.route
        })
        .addTo(this.map)
      this.searchedRoutes.push(route)
      // }
    }
    drawRoutes();
  }

  carIcon = L.icon({
    iconUrl: "assets/directions_car.png",
    iconSize: [20, 20],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });

  redIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });

  greenIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });


  ngAfterViewInit()
    :
    void {
  }

  calculatePrice() {
    this.ridePayService.calculatePrice(this.estimationsSearch.lengthInKm, this.typeOfVehicle).subscribe(value => {
      this.estimationsSearch.price = (Math.round(value * 100) / 100) as unknown as string
    })
  }

  setSelectedCarType(carType: string) {
    this.typeOfVehicle = carType
    if (this.estimationsSearch.time != undefined) {
      this.calculatePrice()
    }
  }

  optimizeRouteByPrice() {
    console.log(this.searchedRoutes)
    this.mapService.optimizeRouteByPrice()
  }

  optimizeRouteByTime() {

  }

}
