import {Component, Input} from '@angular/core';
import {Logout} from "../../../store/actions/authentication.actions";
import {Store} from "@ngxs/store";
import {Router} from "@angular/router";
import {SocialAuthService} from "@abacritt/angularx-social-login";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  @Input() currentPage = 'dashboard';
  @Input() userRole = 'ADMIN';
  isActive = false;

  constructor(private router: Router, private store: Store, private socialAuthService: SocialAuthService) {
  }

  navigate(page: string) {
    this.router.navigate([page]);
  }

  logout() {
    this.socialAuthService.signOut();
    this.store.dispatch(new Logout()).subscribe({});
  }
}
