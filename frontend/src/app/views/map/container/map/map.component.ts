import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {Control, LatLng, Marker, Polyline} from 'leaflet';
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
import {DriverNotification} from "../../../../model/DriverNotification";
import {NotificationDriverComponent} from "../../components/notification-driver/notification-driver.component";
import {RideDriverNotificationDTO} from "../../../../model/RideDriverNotificationDTO";
import {SidenavComponent} from "../../../../shared/sidenav/sidenav/sidenav.component";
import {LeafletRoute} from "../../../../model/LeafletRoute";
import {NavigationDisplay} from "../../../../model/NavigationDisplay";
import {CurrentRide} from "../../../../model/CurrentRide";
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
  estimationsSearch: MapSearchEstimations
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
  @ViewChild(SidenavComponent) sideNavComponent: SidenavComponent;

  constructor(private mapService: MapService, private ridePayService: RidePayService, private rideService: RideService, private router: Router, private store: Store, private carService: CarService) {
    this.searchedRoutes = [];
    this.estimationsSearch = new MapSearchEstimations();
    this.pins = []
    this.routeForDriver = []
    this.selectedRoute = []
    this.routeForCustomer = []
    this.allRoutesSearch = [[]]
    this.optimizedRoute = []
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
    let ws = new SockJS('http://localhost:8000/socket');
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
    this.stompClient.subscribe("/map-updates/update-route-for-selected-car-" + this.loggedUser.email, (message: any) => {
      this.createRouteForSelectedCar(JSON.parse(message.body))
    })
    this.stompClient.subscribe("/notify-driver/decline-ride-" + this.loggedUser.email, (message: any) => {
      let notifications: DriverNotification[] = JSON.parse(message.body)
      this.notificationDriverComponent.removeDriverNotifications(notifications);
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/notify-driver/start-ride-" + this.loggedUser.email, (message: any) => {
      let rideDriverNotificationDTO: RideDriverNotificationDTO = JSON.parse(message.body)
      this.notificationDriverComponent.updateNotificationStartRide(rideDriverNotificationDTO.driverNotification);
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/notify-driver/end-ride-" + this.loggedUser.email, (message: any) => {
      let endRideNotification: DriverNotification = JSON.parse(message.body)
      this.notificationDriverComponent.updateNotificationEndRide(endRideNotification);
      this.initRouteDriver()
    })
    this.stompClient.subscribe("/customer/init-ride-" + this.loggedUser.email, () => {
      this.sideNavComponent.updateNotificationBadge()
      this.initRouteCustomer()
    })
  }

  getWaypoints(currentRide: CurrentRide): L.LatLng[] {
    let positions: L.LatLng[] = [];
    currentRide.positions.forEach((positionInTime) => {
      let position = positionInTime.position
      positions.push(L.latLng(position.y, position.x))
    })
    return positions
  }

  createPins(positionsInTime: PositionInTime[], routeForUser: (Polyline | Control | Marker)[]) {
    let firstPosition = positionsInTime[0].position
    let firstMarker = L.marker([firstPosition.y, firstPosition.x]).addTo(this.map);
    routeForUser.push(firstMarker)
    let lastPosition = positionsInTime[positionsInTime.length - 1].position
    let lastMarker = L.marker([lastPosition.y, lastPosition.x]).addTo(this.map);
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

  createRouteForSelectedCar(ride: any) {
    const showOnMap = (start: Position, end: Position) => {
      let checkPoints: LatLng[] = []
      checkPoints.push(L.latLng(start.y, start.x))
      checkPoints.push(L.latLng(end.y, end.x))
      L.Routing.control({
        waypoints: checkPoints,
        altLineOptions: {
          extendToWaypoints: false,
          missingRouteTolerance: 0,
        },
      }).on('routesfound', (response) => {
        let route = response.routes[0]
        this.rideService.createRouteForSelectedCar(route, ride.driver.car.id).subscribe(() => {
        })
      }).addTo(this.map)
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
        this.carService.getActiveCar(this.loggedUser.email).subscribe((car: ActiveCarResponse) => {
          if (car !== null) {
            let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
            this.pins.push(marker)
          }
        })
      } else {
        activeCars.forEach((car: ActiveCarResponse) => {
          let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
          this.pins.push(marker);
        })
      }
    })
  }

  removeRoutingControls = (routingControls: L.Routing.Control[]) => {
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
      let route = L.Routing.control({
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
        let route = response.routes[0]
        let totalTime = route.summary.totalTime
        this.estimationsSearch.time = secondsToDhms(totalTime)
        this.estimationsSearch.lengthInKm = route.summary.totalDistance / 1000
        if (this.typeOfVehicle != undefined) {
          this.calculatePrice()
        }
      })
        .on('routeselected', (e) => {
          this.selectedRoute[index] = e.route
        })
        .addTo(this.map)
      this.searchedRoutes.push(route)
    }

    this.removeRoutingControls(this.searchedRoutes)

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


  ngAfterViewInit(): void {
  }

  setSelectedCarType(carType: string) {
    this.typeOfVehicle = carType
    if (this.estimationsSearch.time != undefined) {
      this.calculatePrice()
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
    })
  }

  drawOptimizedLine(optimizedRide: LeafletRoute[]) {
    this.allRoutesSearch.forEach((route) => {
      for (let i = 0; i < route.length; i++) {
        if (route[i].coordinates.length === optimizedRide[i].coordinates.length) {
          this.optimizedRoute[i] = optimizedRide[i];
          L.Routing.line(route[i], {
            addWaypoints: false,
            extendToWaypoints: false,
            missingRouteTolerance: 0,
            styles: [{color: 'yellow', opacity: 0.5}]
          }).addTo(this.map);
        }
      }
    })
  }

  setSelectedRouteOptimized() {
    this.selectedRoute = this.optimizedRoute
  }

  calculatePrice() {
    this.ridePayService.calculatePrice(this.estimationsSearch.lengthInKm, this.typeOfVehicle).subscribe(value => {
      this.estimationsSearch.price = (Math.round(value * 100) / 100) as unknown as string
    })
  }
}
