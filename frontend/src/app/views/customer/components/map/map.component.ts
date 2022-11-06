import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import {MapService} from "../../../../services/map.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit, OnInit {

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


  private initMap(): any {
    var map = L.map('map').setView([45.267136, 19.833549], 11);
    var mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(map);
    return map;
  }

  constructor(private mapService: MapService) {
  }

  pins: any;
  myData: any;
  destinations: any;

  ngOnInit(): void {
    this.pins = [];
    this.destinations = []
    var map = this.initMap();
    this.mapService.getActiveCars().subscribe((data) => {
      this.myData = data;
      this.myData.forEach((car: {
        destination: { x: number; y: number; };
        currentPosition: { x: number; y: number; };
      }) => {
        this.destinations.push(car.destination)
        this.destinations.push(L.marker([car.destination.y, car.destination.x], {icon: this.redIcon}).addTo(map))
        this.pins.push(L.marker([car.currentPosition.y, car.currentPosition.x]).addTo(map));
        var marker = L.marker([car.currentPosition.y, car.currentPosition.x], {icon: this.greenIcon}).addTo(map);

        L.Routing.control({
          waypoints: [
            L.latLng(car.currentPosition.y, car.currentPosition.x),
            L.latLng(car.destination.y, car.destination.x)
          ]
        }).on('routesfound', function (e) {
          var routes = e.routes;
          let maxIndex = 0
          let maxValue = 0
          e.routes[0].coordinates.forEach(function (coord: any, index: any) {
            if (index != 0) {
              let lat = (coord.lat - e.routes[0].coordinates[index - 1].lat) ** 2
              let lng = (coord.lng - e.routes[0].coordinates[index - 1].lng) ** 2
              if (lat + lng > maxValue) {
                maxValue = lat + lng
                maxIndex = index
              }
            }
            // var markeraa = L.marker([coord.lat, coord.lng]).addTo(map);
          })
          e.routes[0].instructions.splice(-1)

          function measureDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {  // generally used geo measurement function
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

          let numberOfCoordinates = 1
          let distanceSlots: number[] = []
          for (let i = 0; i < e.routes[0].coordinates.length; i++) {
            if (i == 0) {
              continue
            }
            let prevCoord = e.routes[0].coordinates[i - 1]
            let coord = e.routes[0].coordinates[i]
            let distance = measureDistance(coord.lat, coord.lng, prevCoord.lat, prevCoord.lng)
            if (distance == 0) {
              distanceSlots.push(numberOfCoordinates)
              numberOfCoordinates = 0
            }
            numberOfCoordinates += 1
          }

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
          while (timeSlots.length < e.routes[0].coordinates.length) {
            timeSlots.push(timeSlots[timeSlots.length - 1])
          }

          e.routes[0].coordinates.forEach(function (coord: any, index: any) {
            setTimeout(function () {
              marker.setLatLng([coord.lat, coord.lng]);
            }, 100 * timeSlots[index])
          })
        }).addTo(map);
      })
    })
  }

  ngAfterViewInit(): void {
  }

}
