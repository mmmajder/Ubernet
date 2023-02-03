import {Injectable} from '@angular/core';
import * as L from "leaflet";
import {SearchEstimation} from "../../../../model/SearchEstimation";
import {secondsToDhms} from "../../../../services/utils.service";
import {MapSearchEstimations} from "../../../../model/MapSearchEstimations";
import {RidePayService} from "../../../../services/ride-price.service";

@Injectable({
  providedIn: 'root'
})
export class MapDesignService {

  constructor(private ridePayService: RidePayService) {
  }

  public greenIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });

  public redIcon = new L.Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
  });

  calculatePriceAndTime(estimations: SearchEstimation, estimationsSearch: MapSearchEstimations[], typeOfVehicle: string) {
    estimations = new SearchEstimation()
    let lengthInKM = 0
    let time = 0
    estimationsSearch.forEach((estimation) => {
      lengthInKM += estimation.lengthInKm
      time += estimation.time
    })
    estimations.time = secondsToDhms(time)
    this.ridePayService.calculatePrice(lengthInKM, typeOfVehicle).subscribe(value => {
      estimations.price += (Math.round(value * 100) / 100) as unknown as string
    })
  }
}
