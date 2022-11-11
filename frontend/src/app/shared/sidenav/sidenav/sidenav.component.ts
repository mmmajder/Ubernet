import {Component, OnInit, ViewChild} from '@angular/core';
import {MatMenuTrigger} from "@angular/material/menu";

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {

  isActive: boolean = false;
  @ViewChild(MatMenuTrigger) trigger: MatMenuTrigger | undefined;

  someMethod() {
    this.trigger?.openMenu();
  }

  constructor() { }

  ngOnInit(): void {
  }

  toggle() {
    this.isActive = !this.isActive;
  }

}
