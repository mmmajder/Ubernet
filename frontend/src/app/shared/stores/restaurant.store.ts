import {Injectable} from "@angular/core";
import {ComponentStore, tapResponse} from "@ngrx/component-store";
import {MenuItem, Restaurant, RestaurantService} from "../../services/restaurant.service";
import {exhaustMap, Observable, tap} from "rxjs";
import {SocketService} from "../../services/sockets.service";
import {RemoveMenuItemRequest} from "../../views/restaurant/components/restaurant-details/restaurant-details.component";

export interface RestaurantState {
  restaurant: Restaurant;
}

const INITIAL_STATE: RestaurantState = {
  restaurant: new Restaurant()
}

@Injectable()
export class RestaurantStore extends ComponentStore<RestaurantState> {

  constructor(private restaurantService: RestaurantService, private socketService: SocketService) {
    super(INITIAL_STATE);
  }

  readonly restaurant$: Observable<Restaurant> = this.select(state => state.restaurant);

  getRestaurant = this.effect((restaurantId$: Observable<string>) => restaurantId$
    .pipe(
      exhaustMap(id => {
        return this.restaurantService.findById(id)
          .pipe(tapResponse(
            (response) => this.patchState({restaurant: response}),
            (error) => console.error(error),
          ))
      })
    )
  )

  addMenuItem = this.effect(
    (menuItem$: Observable<MenuItem>) => {
      return menuItem$.pipe(
        exhaustMap((menuItem) =>
          this.restaurantService.addMenuItem(menuItem)
            .pipe(tap({
              next: (response) => {
                this.restaurantUpdater(response);
                this.socketService.webSocketAPI._send(JSON.stringify(response), "/app/addMenuItem")
              },
              error: (error) => console.error(error),
            }))
        )
      )
    });

  readonly restaurantUpdater = this.updater((state: RestaurantState, payload: MenuItem) => ({
    restaurant: {
      ...state.restaurant,
      menuItems: [...state.restaurant.menuItems, payload]
    }
  }));

  readonly removedMenuItem = this.updater((state: RestaurantState, payload: RemoveMenuItemRequest) => ({
    restaurant: {
      ...state.restaurant,
      menuItems: state.restaurant.menuItems.filter(item => item.name !== payload.name)
    }
  }));

  removeMenuItem = this.effect(
    (menuItem$: Observable<RemoveMenuItemRequest>) => {
      return menuItem$.pipe(
        exhaustMap((menuItem) =>
          this.restaurantService.removeMenuItem(menuItem.restaurantId, menuItem.name)
            .pipe(tap({
              next: (response) => {
                this.removedMenuItem(menuItem);
                let item = {
                  "restaurantId": menuItem.restaurantId,
                  "name": menuItem.name
                }
                this.socketService.webSocketAPI._send(JSON.stringify(item), "/app/removeMenuItem")
              },
              error: (error) => console.error(error),
            }))
        )
      )
    });
}
