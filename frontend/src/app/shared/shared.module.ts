import {NgModule} from "@angular/core";
import {NavbarComponent} from './navbar/navbar/navbar.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {CommonModule} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {NavbarStore} from "./stores/navbar.store";
import {RestaurantsStore} from "./stores/restaurants.store";
import {RestaurantStore} from "./stores/restaurant.store";
import { SidenavComponent } from './sidenav/sidenav/sidenav.component';
import {MatBadgeModule} from "@angular/material/badge";

@NgModule({
  declarations: [NavbarComponent, SidenavComponent],
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    CommonModule,
    MatButtonModule,
    MatBadgeModule
  ],
  exports: [NavbarComponent, SidenavComponent],
  providers: [NavbarStore, RestaurantsStore, RestaurantStore]
})
export class SharedModule {

}
