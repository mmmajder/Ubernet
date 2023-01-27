import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageContainerComponent} from "./views/homepage/container/homepage-container/homepage-container.component";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {ChatContainerComponent} from "./views/admin/container/chat-container/chat-container.component";
import {MapComponent} from "./views/map/container/map/map.component";
import {DashboardContainerComponent} from "./views/pages/dashboard-container/dashboard-container.component";
import {ProfileContainerComponent} from "./views/pages/profile-container/profile-container.component";
import {VerifyRegistrationComponent} from "./views/verify/verify-registration/verify-registration.component";
import {RidesHistoryContainerComponent} from "./views/pages/rides-history-container/rides-history-container.component";
import {
  RequestSplitFareMailAcceptComponent
} from "./views/request-ride-accept/request-split-fare-mail-accept/request-split-fare-mail-accept.component";
import {AnalyticsContainerComponent} from "./views/pages/analytics-container/analytics-container.component";
import {ResetPasswordComponent} from "./views/customer/components/reset-password/reset-password.component";
import {UsersContainerComponent} from "./views/admin/container/users-container/users-container.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'dashboard', component: DashboardContainerComponent},
  {path: 'map', component: MapComponent},
  {path: 'chat', component: ChatContainerComponent},
  {path: 'users', component: UsersContainerComponent},
  {path: 'profile', component: ProfileContainerComponent},
  {path: 'rides', component: RidesHistoryContainerComponent},
  {path: 'analytics', component: AnalyticsContainerComponent},
  {path: 'verify/:verificationCode', component: VerifyRegistrationComponent},
  {path: 'reset-password/:resetPasswordCode', component: ResetPasswordComponent},
  {path: 'request-ride/:acceptRideUrl', component: RequestSplitFareMailAcceptComponent},
  {path: '**', component: NotFoundPageComponent},
  // {canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
