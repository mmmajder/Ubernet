import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";
import {UserRole} from "../../../../model/UserRole";
import {ActiveCarResponse} from "../../../../model/ActiveCarResponse";
import {Marker} from "leaflet";
import {Position} from "../../../../model/Position";

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

  constructor(private mapService: MapService) {
    this.userRole = UserRole.CUSTOMER
    this.searchPins = []
  }

  ngOnInit(): void {
    this.initMap()
    this.initPins()
  }

  private initMap() {
    this.map = L.map('map').setView([45.267136, 19.833549], 11);
    let mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(this.map);
  }

  private initPins() {
    this.mapService.getActiveCars().subscribe((activeCars) => {
      activeCars.forEach((car: ActiveCarResponse) => {
        let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map);
        this.initDirections(car, marker)
      })
    })

  }

  initDirections = (car: ActiveCarResponse, marker: Marker<any>) => {
    let positionPosition = L.latLng(car.currentPosition.y, car.currentPosition.x)
    let destinationPosition = L.latLng(car.destinations[0].y, car.destinations[0].x)
    let route = L.Routing.control({
      waypoints: [
        positionPosition,
        destinationPosition
      ],
      routeWhileDragging: false,
      addWaypoints: false,
    }).on('routesfound', (e) => {
      let timeSlots = this.mapService.getTimeSlots(e);   // get time when reaching new location
      while (timeSlots.length < e.routes[0].coordinates.length) {
        timeSlots.push(timeSlots[timeSlots.length - 1])
      }
      e.routes[0].coordinates.forEach((coord: any, index: any) => {
        setTimeout(() => {
          marker.setLatLng([coord.lat, coord.lng]);
          if (index == e.routes[0].coordinates.length - 1) {
            this.setNewPosition(car, marker)
          }
        }, 100 * timeSlots[index])
      })
    }).addTo(this.map)
  }

  setNewPosition(car: ActiveCarResponse, marker: Marker<any>): ActiveCarResponse {
    let returnData = car;
    this.mapService.setNewPositionOfCar(car.carId).subscribe((data) => {
      let getNewDestination = () => {
        this.mapService.getCarById(car.carId).subscribe((newCarData) => {
          if (newCarData.currentPosition.id === newCarData.destinations[0].id) {
            getNewDestination()
          } else {
            this.initDirections(newCarData, marker)
          }
        })
      }
      getNewDestination()
    })
    return returnData;
  }

  getOutputVal(positions: Position[]) {
    positions.forEach((position) => {
      let marker = L.marker([position.y, position.x]).addTo(this.map);
      this.searchPins.push(marker)
    })
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

}
