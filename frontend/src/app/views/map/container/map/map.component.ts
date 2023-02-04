import {Component, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {Control, LatLng, Marker, Polyline} from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";
import {UserRole} from "../../../../model/UserRole";
import {ActiveCarResponse} from "../../../../model/ActiveCarResponse";
import {Place, Position} from "../../../../model/Position";
import {RidePayService} from "../../../../services/ride-price.service";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {RideService} from "../../../../services/ride.service";
import {User, userIsDriver} from "../../../../model/User";
import {CurrentlyLogged} from "../../../../store/actions/loggedUser.actions";
import {ActivatedRoute, Router} from '@angular/router';
import {Store} from "@ngxs/store";
import {CarService} from "../../../../services/car.service";
import {DriverNotification} from "../../../../model/DriverNotification";
import {NotificationDriverComponent} from "../../components/notification-driver/notification-driver.component";
import {RideDriverNotificationDTO} from "../../../../model/RideDriverNotificationDTO";
import {NavbarComponent} from "../../../../shared/sidenav/navbar/navbar.component";
import {LeafletRoute} from "../../../../model/LeafletRoute";
import {NavigationDisplay} from "../../../../model/NavigationDisplay";
import {CurrentRide} from "../../../../model/CurrentRide";
import {PositionInTime} from "../../../../model/PositionInTime";
import {SearchEstimation} from "../../../../model/SearchEstimation";
import {Message} from "stompjs";
import {RideDetails} from "../../../../model/RideDetails";
import {
  SearchDirectionsCustomerComponent
} from "../../components/search-directions-customer/search-directions-customer.component";
import {secondsToDhms} from "../../../../services/utils.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  userRole: UserRole | null
  userRoles = UserRole
  map: L.Map
  estimationsSearch: MapSearchEstimations[]
  estimations: SearchEstimation
  typeOfVehicle: string
  searchedRoutes: L.Routing.Control[];
  pins: Marker[];
  selectedRoute: LeafletRoute[];
  allRoutesSearch: LeafletRoute[][];
  loggedUser: User;
  routeForDriver: (L.Polyline | L.Control | L.Marker)[];
  routeForCustomer: (L.Polyline | L.Marker)[]
  optimizedRoute: LeafletRoute[]
  private stompClient: any;

  @ViewChild(NotificationDriverComponent) notificationDriverComponent: NotificationDriverComponent;
  @ViewChild(NavbarComponent) sideNavComponent: NavbarComponent;
  @ViewChild(SearchDirectionsCustomerComponent) searchDirectionCustomerComponent: SearchDirectionsCustomerComponent;

  constructor(private route: ActivatedRoute, private mapService: MapService, private ridePayService: RidePayService, private rideService: RideService, private router: Router, private store: Store, private carService: CarService) {
    this.searchedRoutes = [];
    this.estimationsSearch = [];
    this.pins = []
    this.routeForDriver = []
    this.selectedRoute = []
    this.routeForCustomer = []
    this.allRoutesSearch = [[]]
    this.optimizedRoute = []
  }

  initializeFavoriteRoute(rideId: string) {
    this.rideService.getById(Number(rideId)).subscribe((ride) => {
      this.searchDirectionCustomerComponent.initFavRoute(ride)
    })
  }

  ngOnInit(): void {
    this.initMap()
    this.initializeWebSocketConnection();
    this.initPins()
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        this.loggedUser = resp.loggedUser;
        if (resp.loggedUser.role === "CUSTOMER") {
          this.userRole = UserRole.CUSTOMER
          this.initRouteCustomer()
        } else if (resp.loggedUser.role === "DRIVER") {
          this.userRole = UserRole.DRIVER
          this.initRouteDriver()
        }
      },
      error: () => {
        this.userRole = this.userRoles.UNAUTHORIZED
        // this.router.navigate(['/'])
      }
    });
    const rideId = this.route.snapshot.paramMap.get('rideId');
    console.log("Milan")
    console.log(rideId)
    if (rideId !== null) {
      this.initializeFavoriteRoute(rideId as string);
    }
  }

  initRouteDriver() {
    this.routeForDriver.forEach(i => {
      if (i instanceof L.Polyline || i instanceof L.Marker)
        this.map.removeLayer(i)
      else
        this.map.removeControl(i)
    });
    this.routeForDriver = []
    this.carService.findCurrentRideByDriverEmail(this.loggedUser.email).subscribe((navigationDisplay) => {
      this.createRouteForDriver(navigationDisplay);
    })
  }

  initRouteCustomer() {
    this.routeForCustomer.forEach((element) => {
      this.map.removeLayer(element)
    })
    this.routeForCustomer = [];
    this.rideService.findScheduledRouteForClient(this.loggedUser.email).subscribe((currentRide) => {
      this.createRouteForCustomer(currentRide);
    })

  }

  initializeWebSocketConnection() {
    const ws = new SockJS('http://localhost:8000/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    this.stompClient.connect({}, () => {
      this.openSocket();
    });
  }

  openSocket() {
    this.stompClient.subscribe("/map-updates/update-vehicle-position", () => {
      this.removePins()
      this.initPins();
    })
    if (this.loggedUser) {
      this.stompClient.subscribe("/map-updates/update-route-for-selected-car-" + this.loggedUser.email, (message: Message) => {
        this.createRouteForSelectedCar(JSON.parse(message.body))
      })
      this.stompClient.subscribe("/notify-driver/decline-ride-" + this.loggedUser.email, (message: Message) => {
        const notifications: DriverNotification[] = JSON.parse(message.body)
        this.notificationDriverComponent.removeDriverNotifications(notifications);
        this.initRouteDriver()
      })
      this.stompClient.subscribe("/notify-driver/start-ride-" + this.loggedUser.email, (message: Message) => {
        const rideDriverNotificationDTO: RideDriverNotificationDTO = JSON.parse(message.body)
        this.notificationDriverComponent.updateNotificationStartRide(rideDriverNotificationDTO.driverNotification);
        this.initRouteDriver()
      })
      this.stompClient.subscribe("/notify-driver/end-ride-" + this.loggedUser.email, (message: Message) => {
        const endRideNotification: DriverNotification = JSON.parse(message.body)
        this.notificationDriverComponent.updateNotificationEndRide(endRideNotification);
        this.initRouteDriver()
      })
      this.stompClient.subscribe("/customer/init-ride-" + this.loggedUser.email, () => {
        this.sideNavComponent.updateNotificationBadge()
        this.initRouteCustomer()
      })
    }
  }

  getWaypoints(currentRide: CurrentRide): L.LatLng[] {
    const positions: L.LatLng[] = [];
    currentRide.positions.forEach((positionInTime) => {
      const position = positionInTime.position
      positions.push(L.latLng(position.y, position.x))
    })
    return positions
  }

  createPins(positionsInTime: PositionInTime[], routeForUser: (Polyline | Control | Marker)[]) {
    const firstPosition = positionsInTime[0].position
    const firstMarker = L.marker([firstPosition.y, firstPosition.x]).addTo(this.map);
    firstMarker.dragging?.disable()
    routeForUser.push(firstMarker)
    const lastPosition = positionsInTime[positionsInTime.length - 1].position
    const lastMarker = L.marker([lastPosition.y, lastPosition.x]).addTo(this.map);
    lastMarker.dragging?.disable()
    routeForUser.push(lastMarker)
  }

  createRouteForDriver(navigationDisplay: NavigationDisplay) {
    if (navigationDisplay.firstApproach != null) {
      this.routeForDriver.push(this.addRouteToMap(this.getWaypoints(navigationDisplay.firstApproach)))
      this.createPins(navigationDisplay.firstApproach.positions, this.routeForDriver)
    }
    if (navigationDisplay.secondApproach != null) {
      this.routeForDriver.push(this.addRouteToMap(this.getWaypoints(navigationDisplay.secondApproach)))
    }
    if (navigationDisplay.firstRide != null) {
      this.routeForDriver.push(this.addRouteToMap(this.getWaypoints(navigationDisplay.firstRide)))
      this.createPins(navigationDisplay.firstRide.positions, this.routeForDriver)
    }
    if (navigationDisplay.secondRide != null) {
      this.routeForDriver.push(this.addRouteToMap(this.getWaypoints(navigationDisplay.secondRide)))
      this.createPins(navigationDisplay.secondRide.positions, this.routeForDriver)
    }
  }

  createRouteForCustomer(currentRide: CurrentRide) {
    if (currentRide != null) {
      this.routeForCustomer.push(this.addRouteToMap(this.getWaypoints(currentRide)))
      this.createPins(currentRide.positions, this.routeForCustomer)
    }
  }

  addRouteToMap(waypoints: L.LatLng[]): L.Polyline {
    return new L.Polyline(waypoints, {
      color: 'red',
      weight: 4,
      opacity: 1,
      smoothFactor: 1
    }).addTo(this.map);
  }

  createRouteForSelectedCar(ride: RideDetails) {
    const showOnMap = (start: Position, end: Position) => {
      const checkPoints: LatLng[] = []
      checkPoints.push(L.latLng(start.y, start.x))
      checkPoints.push(L.latLng(end.y, end.x))
      this.routeForDriver.push(L.Routing.control({
        waypoints: checkPoints,
        routeWhileDragging: false,
        altLineOptions: {
          extendToWaypoints: false,
          missingRouteTolerance: 0,
        },
      }).on('routesfound', (response) => {
        const route = response.routes[0]
        this.rideService.createRouteForSelectedCar(route, ride.driver.car.id).subscribe()
      }).addTo(this.map))
    }
    let start: Position
    let end: Position
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
      if (this.loggedUser!==undefined && userIsDriver(this.loggedUser)) {
        this.carService.getActiveCar(this.loggedUser.email).subscribe((car: ActiveCarResponse) => {
          if (car !== null) {
            const marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map).bindPopup('<p>' + car.driverEmail + '</p>');
            this.pins.push(marker)
          }
        })
      } else {
        activeCars.forEach((car: ActiveCarResponse) => {
          if (car.approachFirstRide !== null || (car.firstRide !== null && !car.firstRide.freeRide)) {
            const marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.redIcon}).addTo(this.map).bindPopup('<p>' + car.driverEmail + '</p>');
            this.pins.push(marker);
          } else {
            const marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map).bindPopup('<p>' + car.driverEmail + '</p>');
            this.pins.push(marker);
          }
        })
      }
    })
  }

  removeRoutingControls = (routingControls: L.Routing.Control[]) => {
    if (routingControls == undefined) {
      return
    }
    routingControls.forEach((routingControl:Control) => {
      this.map.removeControl(routingControl);
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
        this.allRoutesSearch[index] = response.routes
      })
        .on('routeselected', (e) => {
          this.selectedRoute[index] = e.route
          const totalTime = e.route.summary.totalTime
          this.estimationsSearch[index] = new MapSearchEstimations()
          this.estimationsSearch[index].time = totalTime
          this.estimationsSearch[index].lengthInKm = e.route.summary.totalDistance / 1000
          if (this.typeOfVehicle != undefined) {
            this.calculatePriceAndTime()
          }
        })
        .addTo(this.map)
      this.searchedRoutes.push(route)
    }
    this.removeRoutingControls(this.searchedRoutes)
    this.estimationsSearch = []
    for (let i = 1; i < positions.length; i++) {
      drawRoute([
        L.latLng(positions[i - 1].position.y, positions[i - 1].position.x),
        L.latLng(positions[i].position.y, positions[i].position.x)
      ], i - 1)
    }
  }

  greenIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
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

  setSelectedCarType(carType: string) {
    this.typeOfVehicle = carType
    if (this.estimationsSearch[0].time != undefined) {
      this.calculatePriceAndTime()
    }
  }

  optimizeRouteByPrice() {
    this.mapService.optimizeRouteByPrice(this.allRoutesSearch).subscribe((optimizedRide: LeafletRoute[]) => {
      this.drawOptimizedLine(optimizedRide)
    })
  }

  optimizeRouteByTime() {
    this.mapService.optimizeRouteByTime(this.allRoutesSearch).subscribe((optimizedRide: LeafletRoute[]) => {
      this.drawOptimizedLine(optimizedRide)
      this.selectedRoute = this.optimizedRoute
    })
  }

  drawOptimizedLine(optimizedRide: LeafletRoute[]) {
    optimizedRide.forEach((optimizedPath, index) => {
      this.allRoutesSearch[index].forEach((route) => {
          if (route.coordinates.length === optimizedPath.coordinates.length) {
            this.optimizedRoute[index] = optimizedRide[index];
            new L.Polyline(route.coordinates, {
              color: 'yellow',
              weight: 4,
              opacity: 1,
              smoothFactor: 1
            }).addTo(this.map);
          }
        }
      )
    })
  }

  calculatePriceAndTime() {
    this.estimations = new SearchEstimation()
    let lengthInKM = 0
    let time = 0
    this.estimationsSearch.forEach((estimation) => {
      lengthInKM += estimation.lengthInKm
      time += estimation.time
    })
    this.estimations.time = secondsToDhms(time)
    this.ridePayService.calculatePrice(lengthInKM, this.typeOfVehicle).subscribe(value => {
      this.estimations.price += (Math.round(value * 100) / 100) as unknown as string
    })
  }
}
