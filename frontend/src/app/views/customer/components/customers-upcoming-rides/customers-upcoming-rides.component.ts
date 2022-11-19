import {Component, OnInit} from '@angular/core';
import {ReservedRideItem} from "../../../../model/ReservedRideItem";

@Component({
  selector: 'app-customers-upcoming-rides',
  templateUrl: './customers-upcoming-rides.component.html',
  styleUrls: ['./customers-upcoming-rides.component.css']
})
export class CustomersUpcomingRidesComponent implements OnInit {
  rides: ReservedRideItem[] = [{
    'time': '15:10h',
    'timeLeft': '15min',
    'start': 'Bulevar Kralja Petra 5',
    'destination': 'Trzni centar Promenada',
    'friends': [
      {
        'name': 'Pera',
        'photo': '../../assets/default-profile-picture.jpg'
      },
      {
        'name': 'Pera',
        'photo': '../../assets/default-profile-picture.jpg'
      }]},
      {
        'time': '15:10h',
        'timeLeft': '15min',
        'start': 'Bulevar Kralja Petra 5',
        'destination': 'Trzni centar Promenada',
        'friends': [
          {
            'name': 'Pera',
            'photo': '../../assets/default-profile-picture.jpg'
          },
          {
            'name': 'Pera',
            'photo': '../../assets/default-profile-picture.jpg'
          }
        ]
      }];

  constructor() {
  }

  ngOnInit(): void {
  }

}
