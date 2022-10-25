import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {
  favorites = [{
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "end": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "end": "Danila Kiša 12"
    },
    {
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "end": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "end": "Danila Kiša 12"
    },
    {
      "name": "Kuća-poso",
      "start": "Danila Kiša 12",
      "end": "Bulevar Evrope 56"
    },
    {
      "name": "Poso-kuća",
      "start": "Bulevar Evrope 56",
      "end": "Danila Kiša 12"
    }];

  constructor() {
  }

  ngOnInit(): void {
  }

}
