import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-not-found-page',
  templateUrl: './not-authorized-page.component.html',
  styleUrls: ['./not-authorized-page.component.css']
})
export class NotAuthorizedPageComponent {

  constructor(private router: Router) {
  }

  navigate() {
    this.router.navigate(['/']);
  }
}
