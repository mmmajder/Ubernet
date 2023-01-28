import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AuthService} from "../services/auth.service";

@Injectable()
export class CanActivateAuthGuard implements CanActivate {
  constructor(private authenticationService: AuthService, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.authenticationService.isLoggedIn()) {
      return true;
    } else {
      this.router.navigate(['/403']);
      return false;
    }
  }
}
