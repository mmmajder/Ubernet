import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {

  isActive: boolean = false;

  constructor() { }

  ngOnInit(): void {
  }

  toggle() {
    this.isActive = !this.isActive;
  }

}
