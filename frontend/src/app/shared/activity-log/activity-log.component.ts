import {Component, OnInit} from '@angular/core';
import {ActivityLogItem} from "../../model/ActivityLogItem";

@Component({
  selector: 'app-activity-log',
  templateUrl: './activity-log.component.html',
  styleUrls: ['./activity-log.component.css']
})
export class ActivityLogComponent implements OnInit {
  activityLog: ActivityLogItem[] = [{
    "text": "You ordered ride at address: Danila Kiša 12",
    "time": "15:04 12.12.2022."
  },
    {
      "text": "You ordered ride at address: Danila Kiša 12",
      "time": "16:04 13.12.2022."
    },
    {
      "text": "You ordered ride at address: Danila Kiša 12",
      "time": "15:14 14.12.2022."
    }];

  constructor() {
  }

  ngOnInit(): void {
  }

}
