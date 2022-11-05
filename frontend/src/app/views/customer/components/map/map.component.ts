import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import {DomUtil} from "leaflet";
import getPosition = DomUtil.getPosition;
import {MapService} from "../../../../services/map.service";

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit, OnInit {

  private initMap(): any {
    // console.log(this.myData)
    // this.myData.forEach((car: { id: any; }) => {
    //   console.log(car.id)
    // })




    var greenIcon = new L.Icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });
    var redIcon = new L.Icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });
    var map = L.map('map').setView([45.267136, 19.833549], 11);
    var mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
      maxZoom: 18
    }).addTo(map);
    //
    // var taxiIcon = L.icon({
    //   iconUrl: 'assets/Taxi_Icon.png',
    //   iconSize: [50, 50]
    // })
    //
    // var marker = L.marker([45.267136, 19.833549], {icon: taxiIcon}).addTo(map);
    //
    // map.on('click', function (e) {
    //   console.log(e)
    //   var newMarker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);
    //   L.Routing.control({
    //     waypoints: [
    //       L.latLng(45.267136, 19.833549),
    //       L.latLng(e.latlng.lat, e.latlng.lng)
    //     ]
    //   }).on('routesfound', function (e) {
    //     var routes = e.routes;
    //     console.log(routes);
    //
    //     e.routes[0].coordinates.forEach(function (coord: any, index: any) {
    //       setTimeout(function () {
    //         marker.setLatLng([coord.lat, coord.lng]);
    //       }, 100 * index)
    //     })
    //
    //   }).addTo(map);
    // });







    // var marker = L.marker([45.267136, 19.833549]).addTo(map);
    // var positionLat = marker.getLatLng().lat
    // var positionLng = marker.getLatLng().lng
    //
    // var ending = L.marker([45.277136, 19.833549]).addTo(map);
    // var endingLat = ending.getLatLng().lat
    // var endingLng = ending.getLatLng().lng
    //
    // var routes: { coordinates: any[]; };
    //
    // L.Routing.control({
    //   waypoints: [
    //     L.latLng(positionLat, positionLng),
    //     L.latLng(endingLat, endingLng)
    //   ]
    // }).on('routesfound', function (e) {
    //   routes = e.routes;
    //   console.log(routes);
    // })

    // e.routes[0].coordinates.forEach(function (coord: any, index: any) {
    //   setTimeout(function () {
    //     marker.setLatLng([coord.lat, coord.lng]);
    //   }, 100 * index)
    // })

    // var redPointer: L.Marker<any>;

    // function update_position() {
    //   console.log("stigao")
    //   if ((positionLat == endingLat) && (positionLng == endingLng)) {
    //     console.log("BRAVOOO")
    //   } else {
    //     if (!redPointer) {
    //       console.log("stigao1")
    //       redPointer = L.marker([positionLat, positionLng], {icon: redIcon}).addTo(map);
    //     } else {
    //       console.log("stigao2")
    //       redPointer.setLatLng([positionLat + (Math.random() - 0.5) / 10, positionLng + (Math.random() - 0.5) / 10]);
    //       if (routes!=undefined) {
    //         console.log("AAAAA")
    //         console.log("coord ", routes.coordinates)
    //       }
    //
    //       // var coord = routes.coordinates[0]
    //       // redPointer.setLatLng([coord.lat, coord.lng]);
    //       // routes.coordinates.shift()
    //     }
    //     // routes.coordinates.forEach(function (coord: any, index: any) {
    //     //   setTimeout(function () {
    //     //     redPointer.setLatLng([coord.lat, coord.lng]);
    //     //   }, 100 * index)
    //     // })
    //   }
    //
    //   marker.setLatLng([latitude + (Math.random() - 0.5) / 10, longitude + (Math.random() - 0.5) / 10]);
    //   setTimeout(update_position, 1000);
    // }

    // update_position();

    return map;
  }


  constructor(private mapService: MapService) {
  }

  myData: any;

  ngOnInit(): void {
    var map = this.initMap();
    this.mapService.getActiveCars().subscribe((data) => {
      this.myData = data;
      console.log("stigao" )
      this.myData.forEach((car: { currentPosition: { x: number; y: number; }; }) => {
        var ret = L.marker([car.currentPosition.y, car.currentPosition.x]).addTo(map);
        console.log(ret)
      })
    })
  }

  ngAfterViewInit()
    :
    void {
    // this.initMap();
  }

}
