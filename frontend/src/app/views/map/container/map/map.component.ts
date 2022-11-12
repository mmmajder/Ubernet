import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";
import {UserRole} from "../../../../model/UserRole";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit, OnInit {

  userRoles = UserRole
  userRole: UserRole | null

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


  initMap(): any {
    let map = L.map('map').setView([45.267136, 19.833549], 11);
    let mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(map);
    return map;
  }


  address: string;

  onSubmit() {
    return this.address;
  }

  results: string
  addressArr: any

  findAddress() {
    var url = "https://nominatim.openstreetmap.org/search?format=json&limit=3&q=" + this.address
    fetch(url)
      .then(response => response.json())
      .then(data => this.addressArr = data)
      .then(show => L.marker([this.addressArr[0].lat, this.addressArr[0].lon], {icon: this.greenIcon}).addTo(this.map))
      .catch(err => console.log(err))
  }

  constructor(private mapService: MapService) {
    this.userRole = UserRole.CUSTOMER
    // this.userRole = null

  }

  activeCars: any;
  pinsMap = new Map();
  map: any;

  measureDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {  // generally used geo measurement function
    let R = 6378.137; // Radius of earth in KM
    let dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
    let dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
    let a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    let d = R * c;
    return d * 1000; // meters
  }

  ngOnInit(): void {
    this.map = this.initMap();
    this.mapService.getActiveCars().subscribe((data) => {
      this.activeCars = data;
      // console.log(this.activeCars)
      this.activeCars.forEach((car: any) => {
        let that = this
        this.addRouteToMap.call(this, car, that);
      })
    })
  }

  deleteOldPins(car: any) {
    console.log("AAA")
    if (this.pinsMap.has(car.carId)) {
      console.log("BBB")
      this.map.removeLayer(this.pinsMap.get(car.carId)[0])
      this.map.removeControl(this.pinsMap.get(car.carId)[1])
    }
  }


  addRouteToMap(car: any, that: this) {

    let content = L.DomUtil.create("div", "myPopup");
    content.innerHTML = `${car.carId}
      <br> <a style="font-size: small" href="/Details">Click here for details</a>`;
    content.onclick = (e) => {
      alert("Maaajmune")
      e.preventDefault()
    };
    let marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(this.map).bindPopup(content);
    let positionPin = L.latLng(car.currentPosition.y, car.currentPosition.x)
    let destinationPin = L.latLng(car.destinations[0].y, car.destinations[0].x)
    this.deleteOldPins(car)
    let route = L.Routing.control({
      waypoints: [
        positionPin,
        destinationPin
      ]
    }).on('routesfound', function (e) {
      let timeSlots = that.getTimeSlots(e, that);
      while (timeSlots.length < e.routes[0].coordinates.length) {
        timeSlots.push(timeSlots[timeSlots.length - 1])
      }
      e.routes[0].coordinates.forEach(function (coord: any, index: any) {
        setTimeout(function () {
          marker.setLatLng([coord.lat, coord.lng]);
          if (index == e.routes[0].coordinates.length - 1) {
            // console.log("set new position for car")
            // console.log(car.carId)
            that.setNewPosition(that, car);
          }
        }, 100 * timeSlots[index])
      })
    }).addTo(that.map);
    this.pinsMap.set(car.carId, [marker, route])
  }

  setNewPosition(that: this, car: any) {
    that.mapService.setNewPositionOfCar(car.carId).subscribe((data) => {
      // console.log(data)
      let getNewDestination = () => {
        that.mapService.getCarById(car.carId).subscribe((newCarData) => {
          // console.log("Changed destination?")
          // console.log(newCarData)
          // console.log(newCarData.currentPosition.id)
          // console.log(newCarData.destinations[0].id)
          if (newCarData.currentPosition.id === newCarData.destinations[0].id) {
            getNewDestination()
          } else {
            that.addRouteToMap(newCarData, that)
          }
        })
      }
      getNewDestination()
    })
  }

  getTimeSlots(e: any, that: any) {
    e.routes[0].instructions.splice(-1)
    let distanceSlots = that.getDistanceSlots(e, that);
    return that.calculateTimeSlots(distanceSlots, e);
  }

  calculateTimeSlots(distanceSlots: any, e: any) {
    let timeSlots: number[] = []
    for (let i = 0; i < distanceSlots.length; i++) {
      for (let j = 0; j < distanceSlots[i]; j++) {
        let time = e.routes[0].instructions[i].time / distanceSlots[i]
        if (timeSlots.length == 0) {
          timeSlots.push(time)
        } else {
          timeSlots.push(timeSlots[timeSlots.length - 1] + time)
        }
        timeSlots.push()
      }
    }
    return timeSlots;
  }

  getDistanceSlots(e: any, that: any) {
    let numberOfCoordinates = 1
    let distanceSlots: number[] = []
    for (let i = 0; i < e.routes[0].coordinates.length; i++) {
      if (i == 0) {
        continue
      }
      let prevCoord = e.routes[0].coordinates[i - 1]
      let coord = e.routes[0].coordinates[i]
      let distance = that.measureDistance(coord.lat, coord.lng, prevCoord.lat, prevCoord.lng)
      if (distance == 0) {
        distanceSlots.push(numberOfCoordinates)
        numberOfCoordinates = 0
      }
      numberOfCoordinates += 1
    }
    return distanceSlots;
  }

  ngAfterViewInit(): void {
  }

}
