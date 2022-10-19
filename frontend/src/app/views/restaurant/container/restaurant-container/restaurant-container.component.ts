import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {MenuItem, Restaurant} from "../../../../services/restaurant.service";
import {RestaurantStore} from "../../../../shared/stores/restaurant.store";
import {RemoveMenuItemRequest} from "../../components/restaurant-details/restaurant-details.component";

@Component({
  selector: 'app-restaurant-container',
  templateUrl: './restaurant-container.component.html',
  styleUrls: ['./restaurant-container.component.css'],
})
export class RestaurantContainerComponent implements OnInit {

  id: string = '';
  restaurant$: Observable<Restaurant> = this.restaurantStore.restaurant$;

  constructor(private route: ActivatedRoute, private restaurantStore: RestaurantStore) {
  }

  ngOnInit(): void {
    let id = this.route.snapshot.paramMap.get('id');
    if (id != null) {
      this.id = id;
      this.restaurantStore.getRestaurant(id);
    }
  }

  createMenuItem(model: MenuItem) {
    this.restaurantStore.addMenuItem(model);
  }

  removeMenuItem(request: RemoveMenuItemRequest) {
    this.restaurantStore.removeMenuItem(request);
  }
}
