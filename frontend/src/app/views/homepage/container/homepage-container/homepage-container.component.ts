import {Component} from '@angular/core';

@Component({
  selector: 'app-homepage-container',
  templateUrl: './homepage-container.component.html',
  styleUrls: ['./homepage-container.component.css']
})
export class HomepageContainerComponent {

  showForms() {
    document.getElementById("registation-form")?.classList.toggle("fade");
    document.getElementById("login-form")?.classList.toggle("fade");
  }
}
