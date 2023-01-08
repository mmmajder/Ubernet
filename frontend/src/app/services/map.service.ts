import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {ActiveCarResponse} from "../model/ActiveCarResponse";
import {Coordinate} from "../model/Coordinate";

@Injectable({
  providedIn: 'root'
})
export class MapService {

  private readonly mapUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.mapUrl = 'http://localhost:8000/car';
  }

  public getActiveCars(): Observable<ActiveCarResponse[]> {
    return this.http.get<ActiveCarResponse[]>(this.mapUrl + "/active", this.httpOptions);
  }

  public setNewDestinationOfCar(carId: number) {
    return this.http.put<ActiveCarResponse>(this.mapUrl + "/position/", carId, this.httpOptions);
  }

  public getCarById(carId: number): Observable<ActiveCarResponse> {
    return this.http.get<ActiveCarResponse>(this.mapUrl + "/" + carId, this.httpOptions);
  }

  public saveFoundPositionsOfRide(coordinates: Coordinate[], timeSlots: number[]) {
    const data = {
      "coordinates": coordinates,
      "timeSlots": timeSlots
    }
    return this.http.put<ActiveCarResponse>(this.mapUrl + "/save-position/", data, this.httpOptions);
  }


  getTimeSlots(e: { routes: { instructions: any[]; }[]; }) {
    e.routes[0].instructions.splice(-1) //remove start location
    let distanceSlots = this.getDistanceSlots(e);  //get number of points per path
    return this.calculateTimeSlots(distanceSlots, e); // calculate time for every path
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

  getDistanceSlots(e: any) {
    let numberOfCoordinates = 1
    let distanceSlots: number[] = []
    for (let i = 0; i < e.routes[0].coordinates.length; i++) {
      if (i == 0) {
        continue
      }
      let prevCoordinate = e.routes[0].coordinates[i - 1]
      let coordinate = e.routes[0].coordinates[i]
      let distance = this.measureDistance(coordinate.lat, coordinate.lng, prevCoordinate.lat, prevCoordinate.lng)   // calculate distance in m between points
      if (distance == 0) {
        distanceSlots.push(numberOfCoordinates)
        numberOfCoordinates = 0
      }
      numberOfCoordinates += 1
    }
    return distanceSlots;
  }

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

  findAddress(address: string) {
    var url = "https://nominatim.openstreetmap.org/search?format=json&limit=3&q=" + address
    return this.http.get(url);
  }

  public optimizeRouteByPrice() {
    // return this.http.put<ActiveCarResponse>(this.mapUrl + "/position/", carId, this.httpOptions);
  }
}
