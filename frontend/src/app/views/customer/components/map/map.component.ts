import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import {DomUtil} from "leaflet";
import getPosition = DomUtil.getPosition;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit {
  private map: L.Map;

  private initMap(): void {
    var map = L.map('map').setView([45.267136, 19.833549], 11);
    var mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', { attribution: 'Leaflet &copy; ' + mapLink + ', contribution', maxZoom: 18 }).addTo(map);

    var taxiIcon = L.icon({
      iconUrl: 'assets/Taxi_Icon.png',
      iconSize: [50, 50]
    })

    var marker = L.marker([45.267136, 19.833549], { icon: taxiIcon }).addTo(map);

    map.on('click', function (e) {
      console.log(e)
      var newMarker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);
      L.Routing.control({
        waypoints: [
          L.latLng(45.267136, 19.833549),
          L.latLng(e.latlng.lat, e.latlng.lng)
        ]
      }).on('routesfound', function (e) {
        var routes = e.routes;
        console.log(routes);

        e.routes[0].coordinates.forEach(function (coord: any, index: any) {
          setTimeout(function () {
            marker.setLatLng([coord.lat, coord.lng]);
          }, 100 * index)
        })

      }).addTo(map);
    });


    // var map = L.map('map').setView([45.267136, 19.833549], 11);
    // let mapLink = "<a href='http://openstreetmap.org'>OpenStreetMap</a>";
    // L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    //   attribution: 'Leaflet &copy; ' + mapLink + ', contribution',
    //   maxZoom: 18
    // }).addTo(map);
    //
    // var taxiIcon = L.icon({
    //   iconUrl: 'assets/Taxi_Icon.png',
    //   iconSize: [70, 70]
    // })
    //
    // if (!navigator.geolocation) {
    //   console.log("Your browser does not support geolocation feature")
    // } else {
    //   // setInterval(()=>{
    //   navigator.geolocation.getCurrentPosition(getPosition)
    //   // }, 5000)
    // }
    //
    // var marker: L.Layer, circle: L.Layer;
    //
    // function getPosition(position: any) {
    //   console.log(position)
    //   var lat = position.coords.latitude
    //   var long = position.coords.longitude
    //   var accuracy = position.coords.accuracy
    //
    //   if (marker) {
    //     map.removeLayer(marker);
    //   }
    //   if (circle) {
    //     map.removeLayer(circle);
    //   }
    //
    //   marker = L.marker([lat, long]).addTo(map)
    //   circle = L.circle([lat, long], {radius: accuracy}).addTo(map)
    //   console.log(lat, long)
    //   L.Routing.control({
    //     waypoints: [
    //       L.latLng(lat, long),
    //       L.latLng(45.235536, 19.838549)
    //     ]
    //   }).addTo(map);
    //
    //
    //   var markerIcon = L.marker([lat, long], {icon: taxiIcon}).addTo(map)

      // map.on('click', function (e) {
      //   console.log(e)
      //   var newMarker = L.marker([e.latlng.lat, e.latlng.lng]).addTo(map);
      //   L.Routing.control({
      //     waypoints: [
      //       L.latLng(44.235536, 19.838549),
      //       L.latLng(e.latlng.lat, e.latlng.lng),
      //     ]
      //   })
      //     .on('routesfound', function (e) {
      //       console.log(e)
      //       e.routes[0].coordinates.forEach(function (coord:any, index:any) {
      //         setTimeout(() => {
      //           newMarker.setLatLng([coord.lat, coord.lng])
      //         }, 100 * index)
      //       })
      //     })
      //
      //
      //     .addTo(map)
  //     })
  //   }
  //
  //
  }

  constructor() {
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

}
