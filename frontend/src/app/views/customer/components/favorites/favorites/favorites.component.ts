import {Component} from '@angular/core';
import {FavoriteRouteItem} from "../../../../../model/FavoriteRoute";
import {FavoriteRoutesService} from "../../../../../services/favorite-routes.service";
import {CurrentlyLogged} from "../../../../../store/actions/loggedUser.actions";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent {
  customerEmail: string;
  favorites: FavoriteRouteItem[] = [];

  constructor(private router: Router, private service: FavoriteRoutesService, private store: Store) {
    this.store.dispatch(new CurrentlyLogged()).subscribe({
      next: (resp) => {
        if (resp.loggedUser.role == "CUSTOMER") {
          this.customerEmail = resp.loggedUser.email;
          this.getFavorites();
        }
      }
    });
  }

  getFavorites() {
    this.service.getFavoriteRoutes(this.customerEmail).subscribe({
      next: (value: FavoriteRouteItem[]) => this.favorites = value,
      error: err => console.log(err)
    })
  }

  removeFromFavorites(rideId: number) {
    this.service.removeFromFavoriteRoutes(this.customerEmail, rideId).subscribe({
      next: () => this.removeRide(rideId),
      error: err => console.log(err)
    })
  }

  private removeRide(rideId: number) {
    for (let i = 0; i < this.favorites.length; i++) {
      if (this.favorites[i].rideId === rideId) {
        this.favorites.splice(i, 1);
      }
    }
  }

  orderRide(rideId: number) {
    this.router.navigate(["/map", rideId])
  }
}
