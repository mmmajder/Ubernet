import {Component, OnInit} from '@angular/core';
import {RestaurantsStore} from "../../../shared/stores/restaurants.store";
import {Observable} from "rxjs";
import {Restaurant} from "../../../services/restaurant.service";
import {RestaurantRequestModel} from "../components/create-restaurant-dialog/create-restaurant-dialog.component";

@Component({
  selector: 'app-restaurants-container',
  templateUrl: './restaurants-container.component.html',
  styleUrls: ['./restaurants-container.component.css'],
})
export class RestaurantsContainerComponent implements OnInit {

  restaurants$: Observable<Restaurant[]> = this.restaurantsStore.restaurants$;

  constructor(private restaurantsStore: RestaurantsStore) {
  }

  ngOnInit(): void {
    this.restaurantsStore.getRestaurants();
  }

  ngAfterViewInit() {
  }

  createRestaurant(model: RestaurantRequestModel) {
    this.restaurantsStore.createRestaurant(model);
  }

}
