import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
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
import {DriverNotification} from "../../../../model/DriverNotification";
import {NotificationDriverComponent} from "../../components/notification-driver/notification-driver.component";
import {RideDetails} from "../../../../model/RideDetails";
import {RideDriverNotificationDTO} from "../../../../model/RideDriverNotificationDTO";
import {CurrentRide} from "../../../../model/CurrentRide";
import {SidenavComponent} from "../../../../shared/sidenav/sidenav/sidenav.component";
import {LeafletRoute} from "../../../../model/LeafletRoute";
import {NavigationDisplay} from "../../../../model/NavigationDisplay";
import {RouteDTO} from "../../../../model/RouteDTO";

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
  selectedRoute: LeafletRoute[];
  loggedUser: User;
  routeForSelectedCar: L.Routing.Control[];
  routeCustomer: L.Routing.Control[]

  @ViewChild(NotificationDriverComponent) notificationDriverComponent: NotificationDriverComponent;
  @ViewChild(SidenavComponent) sideNavComponent: SidenavComponent;

  constructor(private mapService: MapService, private ridePayService: RidePayService, private rideService: RideService, private router: Router, private store: Store, private carService: CarService) {
    this.searchPins = [];
    this.totalTime = 0;
    this.searchedRoutes = [];
    this.estimationsSearch = new MapSearchEstimations();
    this.pins = []
    this.directionRoutes = new Map<number, L.Control>;
    this.routeForSelectedCar = []
    this.selectedRoute = []
    this.routeCustomer = []
  }

  ngOnInit(): void {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.loggedUser = resp.loggedUser;
        this.initMap()
        this.initializeWebSocketConnection();
        this.initPins()
        if (resp.loggedUser.role === "CUSTOMER") {
          this.userRole = UserRole.CUSTOMER
          this.initRouteCustomer()
        } else if (resp.loggedUser.role === "DRIVER") {
          this.userRole = UserRole.DRIVER
          this.initRouteDriver()

        }

      },
      error: () => this.router.navigate(['/'])
    });
  }

  initRouteDriver() {
    this.routeForSelectedCar.forEach(i => this.map.removeControl(i));
    this.routeForSelectedCar = []
    this.carService.findCurrentRideByDriverEmail(this.loggedUser.email).subscribe((navigationDisplay) => {
      this.createRouteForDriver(navigationDisplay);
    })
  }

  initRouteCustomer() {
    if (this.routeCustomer.length != 0)
      this.map.removeControl(this.routeCustomer[0]);
    this.routeCustomer = [];
    this.rideService.findScheduledRouteForClient(this.loggedUser.email).subscribe((navigationDisplay) => {
      this.createRouteForCustomer(navigationDisplay);
    })

  }

  reinitializeDirectionsForDrivers() {
    for (const [key, value] of this.directionRoutes) {
      this.map.removeControl(value)
    }
  }

  initializeWebSocketConnection() {
    const ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/map-updates/update-vehicle-position", (message: any) => {
      this.reinitializeDirectionsForDrivers();
      this.removePins()
      this.initPins();
    })
    this.stompClient.subscribe("/map-updates/update-route-for-selected-car-" + this.loggedUser.email, (message: any) => {
      this.createRouteForSelectedCar(JSON.parse(message.body))
    })
    this.stompClient.subscribe("/notify-driver/decline-ride-" + this.loggedUser.email, (message: any) => {
      this.notificationDriverComponent.removeDriverNotifications(JSON.parse(message.body));
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/notify-driver/start-ride-" + this.loggedUser.email, (message: any) => {
      const rideDriverNotificationDTO: RideDriverNotificationDTO = JSON.parse(message.body)
      this.notificationDriverComponent.updateNotificationStartRide(rideDriverNotificationDTO.driverNotification);
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/notify-driver/end-ride-" + this.loggedUser.email, (message: any) => {
      const endRideNotification: DriverNotification = JSON.parse(message.body)
      this.notificationDriverComponent.updateNotificationEndRide(endRideNotification);
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/customer/init-ride-" + this.loggedUser.email, (message: any) => {
      this.sideNavComponent.updateNotificationBadge()
      this.initRouteCustomer()
    })
  }

  createRouteForDriver(navigationDisplay: NavigationDisplay) {
    console.log(navigationDisplay)
    if (navigationDisplay.firstApproach != null) {
      const start = navigationDisplay.firstApproach.positions[0].position
      const end = navigationDisplay.firstApproach.positions[navigationDisplay.firstApproach.positions.length - 1].position
      this.routeForSelectedCar.push(this.addRouteToMap([L.latLng(start.y, start.x), L.latLng(end.y, end.x)], 0))
    }
    if (navigationDisplay.secondApproach != null) {
      const start = navigationDisplay.secondApproach.positions[0].position
      const end = navigationDisplay.secondApproach.positions[navigationDisplay.secondApproach.positions.length - 1].position
      this.routeForSelectedCar.push(this.addRouteToMap([L.latLng(start.y, start.x), L.latLng(end.y, end.x)], 0))
    }
    if (navigationDisplay.firstRide != null) {
      const checkPoints = navigationDisplay.firstRide.checkPoints;
      checkPoints.forEach((place, index: number) => {
        if (index != 0) {
          this.routeForSelectedCar.push(this.addRouteToMap([L.latLng(checkPoints[index - 1].position.y, checkPoints[index - 1].position.x), L.latLng(place.position.y, place.position.x)], navigationDisplay.firstRide.numberOfRoute[index - 1].number))
        }
      })
    }
    if (navigationDisplay.secondRide != null) {
      const checkPoints = navigationDisplay.secondRide.checkPoints;
      checkPoints.forEach((place, index: number) => {
        if (index != 0) {
          this.routeForSelectedCar.push(this.addRouteToMap([L.latLng(checkPoints[index - 1].position.y, checkPoints[index - 1].position.x), L.latLng(place.position.y, place.position.x)], navigationDisplay.secondRide.numberOfRoute[index - 1].number))
        }
      })
    }
  }

  createRouteForCustomer(route: RouteDTO) {
    if (route != null) {
      const checkPoints = route.checkPoints;
      checkPoints.forEach((place, index: number) => {
        if (index != 0) {
          this.routeCustomer.push(this.addRouteToMap([L.latLng(checkPoints[index - 1].position.y, checkPoints[index - 1].position.x), L.latLng(place.position.y, place.position.x)], route.numberOfRoute[index - 1].number))
        }
      })
    }
  }

  addRouteToMap(waypoints: L.LatLng[], numberOfRoute: number): L.Routing.Control {
    return L.Routing.control({
      waypoints: waypoints,
      altLineOptions: {
        extendToWaypoints: false,
        missingRouteTolerance: 0,
        styles: [
          {color: 'blue', opacity: 0.5}
        ],
      },
      showAlternatives: true,
      useZoomParameter: false,
      fitSelectedRoutes: false,
    }).on('routesfound', (response) => {
      console.log(response)
      const route = response.routes[numberOfRoute]
    })
      .addTo(this.map)

    // })
  }

  createRouteForSelectedCar(ride: any) {
    let start: Position
    let end: Position

    const showOnMap = (start: Position, end: Position) => {
      const checkPoints: LatLng[] = []
      checkPoints.push(L.latLng(start.y, start.x))
      checkPoints.push(L.latLng(end.y, end.x))
      this.routeForSelectedCar.push(L.Routing.control({
        waypoints: checkPoints,
        altLineOptions: {
          extendToWaypoints: false,
          missingRouteTolerance: 0,
        },
      }).on('routesfound', (response) => {
        const route = response.routes[0]
        console.log("updateCarRoute")
        console.log(route)
        // localStorage.setItem("currentRide", JSON.stringify(route))
        this.rideService.createRouteForSelectedCar(route, ride.driver.car.id).subscribe(() => {
          console.log("STIGAO sam")
        })
      }).addTo(this.map))
    }
    console.log(ride)
    if (ride.driver.car.navigation.secondRide === null) {
      start = ride.driver.car.position
      end = ride.driver.car.navigation.firstRide.positions[0].position
    } else {
      start = this.rideService.getLastPosition(ride.driver.car.navigation.firstRide.positions)
      end = ride.driver.car.navigation.secondRide.positions[0].position
    }
    showOnMap(start, end)
  }

  private initMap() {
    this.map = L.map('map').setView([45.267136, 19.833549], 11);
    const mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(this.map);
  }

  removePins() {
    if (this.pins.length != 0)
      this.pins.forEach((marker: Marker) => {
        this.map.removeLayer(marker)
      })
  }

  initPins() {
    this.mapService.getActiveCars().subscribe((activeCars) => {
      if (userIsDriver(this.loggedUser)) {
        const car = this.loggedUser.car
        console.log(car)
        // if (car.driver.driverDailyActivity.isActive) {
        this.carService.getActiveCar(this.loggedUser.email).subscribe((car: ActiveCarResponse) => {
          if (car !== null) {
            const marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
            this.pins.push(marker)
            if (car.currentRide !== null)
              this.initDirections(car, marker)
          }
        })
        // }
      } else {
        activeCars.forEach((car: ActiveCarResponse) => {
          const marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
          this.pins.push(marker);
        })
      }
    })

  }

  initDirections = (car: ActiveCarResponse, marker: Marker<any>) => {
    const waypoints = []
    console.log(car)
    const positionsInTime: PositionInTime[] = car.currentRide.positions
    const last = positionsInTime.length - 1
    waypoints.push(L.latLng(positionsInTime[0].position.y, positionsInTime[0].position.x))
    waypoints.push(L.latLng(positionsInTime[last].position.y, positionsInTime[last].position.x))


    const route = L.Routing.control({
      waypoints: waypoints,
      routeWhileDragging: false,
      addWaypoints: false,
      fitSelectedRoutes: false,
    }).on('routesfound', (response) => {
      const timeSlots = this.mapService.getTimeSlots(response);   // get time when reaching new location
      this.mapService.saveFoundPositionsOfRide(response.routes[0].coordinates, timeSlots)
      while (timeSlots.length < response.routes[0].coordinates.length) {
        timeSlots.push(timeSlots[timeSlots.length - 1])
      }
    }).addTo(this.map)
    route.on('add', (e) => {
      const waypoints = route.getWaypoints();
      const markers = e.target._markers;
      for (let i = 0; i < markers.length; i++) {
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
    const drawRoute = (checkPoints: L.LatLng[], index: number) => {
      const route = L.Routing.control({
        waypoints: checkPoints,
        altLineOptions: {
          extendToWaypoints: false,
          missingRouteTolerance: 0,
          styles: [
            {color: 'blue', opacity: 0.5}
          ],
        },
        routeWhileDragging: false,
        addWaypoints: false,
        showAlternatives: true,
        useZoomParameter: false,
      }).on('routesfound', (response) => {
        this.selectedRoute[index] = response.routes
        const route = response.routes[0]
        this.totalTime = route.summary.totalTime
        this.estimationsSearch.time = secondsToDhms(this.totalTime)
        this.estimationsSearch.lengthInKm = route.summary.totalDistance / 1000
        if (this.typeOfVehicle != undefined) {
          this.calculatePrice()
        }
      })
        .on('routeselected', (e) => {
          const route = e.route
          // this.selectedRoute = e.route
          this.selectedRoute[index] = e.route
        })
        .addTo(this.map)

      // route._selectedRoute = route._routes[1]
      this.searchedRoutes.push(route)
    }

    this.removeRoutingControls(this.searchedRoutes)

    const checkPoints: LatLng[] = []
    for (let i = 1; i < positions.length; i++) {
      drawRoute([
        L.latLng(positions[i - 1].position.y, positions[i - 1].position.x),
        L.latLng(positions[i].position.y, positions[i].position.x)
      ], i - 1)
    }
  }

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


  ngAfterViewInit(): void {
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
    this.mapService.optimizeRouteByPrice(this.selectedRoute).subscribe((optimizedRide: CurrentRide) => {

    })
  }

  optimizeRouteByTime() {

  }


}
