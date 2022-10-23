import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-homepage-container',
  templateUrl: './homepage-container.component.html',
  styleUrls: ['./homepage-container.component.css']
})
export class HomepageContainerComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  showForms() {
    document.getElementById("registation-form")?.classList.toggle("fade");
    document.getElementById("login-form")?.classList.toggle("fade");
  }

}
