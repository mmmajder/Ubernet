import {Injectable} from "@angular/core";
import {ComponentStore, tapResponse} from "@ngrx/component-store";
import {Restaurant, RestaurantService} from "../../services/restaurant.service";
import {exhaustMap, Observable, tap} from "rxjs";
import {RestaurantRequestModel} from "../../views/restaurants/components/create-restaurant-dialog/create-restaurant-dialog.component";
import {SocketService} from "../../services/sockets.service";

export interface RestaurantsState {
  restaurants: Restaurant[];
}

const INITIAL_STATE: RestaurantsState = {
  restaurants: []
}

@Injectable()
export class RestaurantsStore extends ComponentStore<RestaurantsState> {

  constructor(private restaurantService: RestaurantService, private socketService: SocketService) {
    super(INITIAL_STATE);
  }

  readonly restaurants$: Observable<Restaurant[]> = this.select(state => state.restaurants);

  getRestaurants = this.effect(trigger$ => trigger$
    .pipe(
      exhaustMap(_ => {
        return this.restaurantService.findAll()
          .pipe(tapResponse(
            (response) => this.patchState({restaurants: response}),
            (error) => console.error(),
          ))
      })
    )
  )

  createRestaurant = this.effect(
    (restaurantRequest$: Observable<RestaurantRequestModel>) => {
      return restaurantRequest$.pipe(
        exhaustMap((restaurant) =>
          this.restaurantService.createRestaurant(restaurant)
            .pipe(tap({
              next: (response) => {
                this.addRestaurant(response);
                this.socketService.webSocketAPI._send(JSON.stringify(restaurant), "/app/hello");
              },
              error: (error) => console.error(),
            }))
        )
      )
    });

  readonly addRestaurant = this.updater((state, restaurant: Restaurant) => ({
    restaurants: [...state.restaurants, restaurant],
  }));

}
