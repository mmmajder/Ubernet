import {Component} from '@angular/core';
import {FavoriteRoute} from "../../../../../model/FavoriteRoute";

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent {
  favorites: FavoriteRoute[] = [{
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "destination": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "destination": "Danila Kiša 12"
    },
    {
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "destination": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "destination": "Danila Kiša 12"
    },
    {
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "destination": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "destination": "Danila Kiša 12"
    }];

}
