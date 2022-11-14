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
import {SidenavComponent} from './sidenav/sidenav/sidenav.component';
import {MatBadgeModule} from "@angular/material/badge";
import {UserChatComponent} from "./user-chat/user-chat.component";
import {MatListModule} from "@angular/material/list";
import {MessageComponent} from "./message/message.component";
import {NewMessageComponent} from "./new-message/new-message.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ProfileDataComponent} from "./profile-edit/profile-data/profile-data.component";
import {ChangePasswordComponent} from "./profile-edit/change-password/change-password.component";
import {MatCardModule} from "@angular/material/card";

@NgModule({
  declarations: [NavbarComponent, SidenavComponent, UserChatComponent, MessageComponent, NewMessageComponent, ProfileDataComponent, ChangePasswordComponent],
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    CommonModule,
    MatButtonModule,
    MatBadgeModule,
    MatListModule,
    MatTooltipModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatCardModule,
  ],
  exports: [NavbarComponent, SidenavComponent, MessageComponent, NewMessageComponent, ProfileDataComponent, ChangePasswordComponent],
  providers: [NavbarStore, RestaurantsStore, RestaurantStore]
})
export class SharedModule {
}
