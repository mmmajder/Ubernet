import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
import {Coordinate} from "../model/Coordinate";
import {CurrentRide} from "../model/CurrentRide";
import {LeafletRoute} from "../model/LeafletRoute";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private readonly carUrl: string;
  private readonly mapUrl: string

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.carUrl = 'http://localhost:8000/car';
    this.mapUrl = 'http://localhost:8000/map'
  }

  public getActiveCars(): Observable<ActiveCarResponse[]> {
    return this.http.get<ActiveCarResponse[]>(this.carUrl + "/active", this.httpOptions);
  }

  public optimizeRouteByPrice(selectedRoute: LeafletRoute[][]): Observable<LeafletRoute[]> {
    return this.http.put<LeafletRoute[]>(this.mapUrl + "/optimize-by-price", selectedRoute, this.httpOptions);
  }

  public optimizeRouteByTime(selectedRoute: LeafletRoute[][]): Observable<LeafletRoute[]> {
    return this.http.put<LeafletRoute[]>(this.mapUrl + "/optimize-by-time", selectedRoute, this.httpOptions);
  }

  public saveFoundPositionsOfRide(coordinates: Coordinate[], timeSlots: number[]) {
    const data = {
      "coordinates": coordinates,
      "timeSlots": timeSlots
    }
    return this.http.put<ActiveCarResponse>(this.carUrl + "/save-position/", data, this.httpOptions);
  }


  getTimeSlots(e: { routes: { instructions: any[]; }[]; }) {
    e.routes[0].instructions.splice(-1) //remove start location
    const distanceSlots = this.getDistanceSlots(e);  //get number of points per path
    return this.calculateTimeSlots(distanceSlots, e); // calculate time for every path
  }

  calculateTimeSlots(distanceSlots: any, e: any) {
    const timeSlots: number[] = []
    for (let i = 0; i < distanceSlots.length; i++) {
      for (let j = 0; j < distanceSlots[i]; j++) {
        const time = e.routes[0].instructions[i].time / distanceSlots[i]
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

  getDistanceSlots(e: any) {
    let numberOfCoordinates = 1
    const distanceSlots: number[] = []
    for (let i = 0; i < e.routes[0].coordinates.length; i++) {
      if (i == 0) {
        continue
      }
      const prevCoordinate = e.routes[0].coordinates[i - 1]
      const coordinate = e.routes[0].coordinates[i]
      const distance = this.measureDistance(coordinate.lat, coordinate.lng, prevCoordinate.lat, prevCoordinate.lng)   // calculate distance in m between points
      if (distance == 0) {
        distanceSlots.push(numberOfCoordinates)
        numberOfCoordinates = 0
      }
      numberOfCoordinates += 1
    }
    return distanceSlots;
  }

  measureDistance(lat1: number, lon1: number, lat2: number, lon2: number): number {  // generally used geo measurement function
    const R = 6378.137; // Radius of earth in KM
    const dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
    const dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const d = R * c;
    return d * 1000; // meters
  }

  findAddress(address: string) {
    const url = "https://nominatim.openstreetmap.org/search?format=json&limit=3&q=" + address
    return this.http.get(url);
  }



}
