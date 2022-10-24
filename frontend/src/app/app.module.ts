import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SharedModule} from './shared/shared.module';
import {MatInputModule} from '@angular/material/input';
import {MatSliderModule} from '@angular/material/slider';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {RestaurantService} from "./services/restaurant.service";
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatDialogModule} from "@angular/material/dialog";
import {RestaurantsModule} from "./views/restaurants/restaurants.module";
import {RestaurantModule} from "./views/restaurant/restaurant.module";
import {NotificationsService} from "./services/notifications.service";
import {SocketService} from "./services/sockets.service";
import {HomepageModule} from "./views/homepage/homepage.module";
import {AuthService} from "./services/auth.service";
import { NotFoundPageComponent } from './views/404/not-found-page/not-found-page.component';
import {DriverModule} from "./views/driver/driver.module";
import {CustomerModule} from "./views/customer/customer.module";
import {AdminModule} from "./views/admin/admin.module";

@NgModule({
  declarations: [
    AppComponent,
    NotFoundPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    MatInputModule,
    MatSliderModule,
    MatButtonModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    FormsModule,
    MatCardModule,
    MatAutocompleteModule,
    MatToolbarModule,
    MatIconModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDialogModule,
    RestaurantsModule,
    RestaurantModule,
    HomepageModule,
    DriverModule,
    CustomerModule,
    AdminModule
  ],
    exports: [
    ],
  providers: [RestaurantService, AuthService, NotificationsService, SocketService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
