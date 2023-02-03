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
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {MatDialogModule} from "@angular/material/dialog";
import {NotificationsService} from "./services/notifications.service";
import {HomepageModule} from "./views/homepage/homepage.module";
import {AuthService} from "./services/auth.service";
import {NotFoundPageComponent} from './views/404/not-found-page/not-found-page.component';
import {DriverModule} from "./views/driver/driver.module";
import {CustomerModule} from "./views/customer/customer.module";
import {AdminModule} from "./views/admin/admin.module";
import {MapModule} from "./views/map/map.module";
import {NgxsModule} from '@ngxs/store';
import {AuthState} from "./store/states/auth.state";
import {LoggedUserState} from "./store/states/loggedUser.state";
import {PagesModule} from "./views/pages/pages.module";
import {VerifyRegistrationComponent} from './views/verify/verify-registration/verify-registration.component';
import {CustomersState} from "./store/states/customers.state";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatSelectModule} from "@angular/material/select";
import {TokensState} from "./store/states/tokens.state";
import {
  RequestSplitFareMailAcceptComponent
} from './views/request-ride-accept/request-split-fare-mail-accept/request-split-fare-mail-accept.component';
import {
  RideSplitFareDialogComponent
} from './views/request-ride-accept/ride-split-fare-dialog/ride-split-fare-dialog.component';
import {NotAuthorizedPageComponent} from "./views/403/not-authorized-page/not-authorized-page.component";
import {AuthGuard} from "./model/auth-guard.service";
import {SocialLoginModule, SocialAuthServiceConfig} from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider,
} from '@abacritt/angularx-social-login';

@NgModule({
  declarations: [
    AppComponent,
    NotFoundPageComponent,
    NotAuthorizedPageComponent,
    VerifyRegistrationComponent,
    RequestSplitFareMailAcceptComponent,
    RideSplitFareDialogComponent,
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
    HomepageModule,
    BrowserAnimationsModule,
    DriverModule,
    CustomerModule,
    AdminModule,
    MapModule,
    PagesModule,
    NgxsModule.forRoot([AuthState, LoggedUserState, CustomersState, TokensState]),
    MatProgressSpinnerModule,
    MatSelectModule,
    SocialLoginModule
  ],
  exports: [],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider(
              '806860450609890'
            )
          },
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              "409257141396-5kl1dff2s24dip94g2kql41tgr7j0jik.apps.googleusercontent.com",
            )
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    AuthService, NotificationsService, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule {
}
