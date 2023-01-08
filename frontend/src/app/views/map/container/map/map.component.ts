import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import {LatLng, Marker} from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";
import {UserRole} from "../../../../model/UserRole";
import {ActiveCarResponse} from "../../../../model/ActiveCarResponse";
import {Position} from "../../../../model/Position";
import {secondsToDhms} from "../../../../services/utils.service";
import {RidePayService} from "../../../../services/ride-price.service";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

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
  directionRoutes: any;
  private stompClient: any;
  private pins: Marker[];

  constructor(private mapService: MapService, private ridePayService: RidePayService) {
    //TODO getlogged user
    this.userRole = UserRole.CUSTOMER;
    this.searchPins = [];
    this.totalTime = 0;
    this.searchedRoutes = [];
    this.directionRoutes = {};
    this.estimationsSearch = new MapSearchEstimations();
    this.pins = []
  }

  ngOnInit(): void {
    this.initMap()
    this.initializeWebSocketConnection();
    this.initPins()
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
      console.log(message)
      this.removePins()
      this.initPins();
    })
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
    if (this.pins!=[])
      this.pins.forEach((marker: Marker) => {
        this.map.removeLayer(marker)
      })
  }

  private initPins() {
    this.mapService.getActiveCars().subscribe((activeCars) => {
      if (this.userRole == UserRole.DRIVER) {
        //TODO get car of logged user
        let car = activeCars[0]
        let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
        this.initDirections(car, marker)
      } else {
        activeCars.forEach((car: ActiveCarResponse) => {
          console.log(car)
          let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
          this.pins.push(marker);
          // this.initDirections(car, marker)
        })
      }
    })

  }

  initDirections = (car: ActiveCarResponse, marker: Marker<any>) => {
    let waypoints = [L.latLng(car.currentPosition.y, car.currentPosition.x)]
    car.currentRide.destinations.forEach((destination: Position) => {
      waypoints.push(L.latLng(destination.y, destination.x))
    })
    let route = L.Routing.control({
      waypoints: waypoints,
      routeWhileDragging: false,
      addWaypoints: false,
      fitSelectedRoutes: false,
    }).on('routesfound', (response) => {
      let timeSlots = this.mapService.getTimeSlots(response);   // get time when reaching new location
      this.mapService.saveFoundPositionsOfRide(response.routes[0].coordinates, timeSlots)
      while (timeSlots.length < response.routes[0].coordinates.length) {
        timeSlots.push(timeSlots[timeSlots.length - 1])
      }
      response.routes[0].coordinates.forEach((coordinate: any, index: any) => {
        setTimeout(() => {
          marker.setLatLng([coordinate.lat, coordinate.lng]);
          if (index == response.routes[0].coordinates.length - 1) {
            // this.setNewDestination(car, marker)
          }
        }, 100 * timeSlots[index])
      })
    }).addTo(this.map)
    this.directionRoutes[car.carId] = [route]
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

  drawSearchedRoute(positions: Position[]) {
    this.removeRoutingControls(this.searchedRoutes)

    let checkPoints: LatLng[] = []
    for (let i = 0; i < positions.length; i++) {
      checkPoints.push(L.latLng(positions[i].y, positions[i].x))
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
        .on('routeselected', function (e) {
          var route = e.route
          console.log(route)
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
    this.mapService.optimizeRouteByPrice()
  }

  optimizeRouteByTime() {

  }
}
