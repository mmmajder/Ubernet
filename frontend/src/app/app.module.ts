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
import {GoogleLoginProvider, SocialAuthServiceConfig, SocialLoginModule} from "angularx-social-login";
import {NotFoundPageComponent} from './views/404/not-found-page/not-found-page.component';
import {DriverModule} from "./views/driver/driver.module";
import {CustomerModule} from "./views/customer/customer.module";
import {AdminModule} from "./views/admin/admin.module";
import {PopupService} from "./services/popup.service";
import { SearchDirectionsComponent } from './views/unauthenticated/components/search-directions/search-directions.component';
import {UnauthenticatedModule} from "./views/unauthenticated/unauthenticated.module";


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
    SocialLoginModule,
    BrowserAnimationsModule,
    DriverModule,
    CustomerModule,
    AdminModule,
    UnauthenticatedModule
  ],
  exports: [
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '263337550240-ilitbfe2sqc3v0vlc61tiuu5bb3no8f6.apps.googleusercontent.com',
              {
                scope: 'profile email',
                plugin_name: 'login' //you can use any name here
              }
            )
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    RestaurantService, AuthService, NotificationsService, SocketService, PopupService],
  bootstrap: [AppComponent]
})

export class AppModule {
}
