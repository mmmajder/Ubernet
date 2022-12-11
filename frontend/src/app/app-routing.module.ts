import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageContainerComponent} from "./views/homepage/container/homepage-container/homepage-container.component";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {ChatContainerComponent} from "./views/admin/container/chat-container/chat-container.component";
import {MapComponent} from "./views/map/container/map/map.component";
import {DriversComponent} from "./views/admin/components/drivers/drivers.component";
import {AuthGuard} from "./auth.guard";
import {DashboardContainerComponent} from "./views/pages/dashboard-container/dashboard-container.component";
import {ProfileContainerComponent} from "./views/pages/profile-container/profile-container.component";
import {VerifyRegistrationComponent} from "./views/verify/verify-registration/verify-registration.component";
import {RidesHistoryComponent} from "./views/pages/rides-history/rides-history.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'dashboard', component: DashboardContainerComponent},
  {path: 'map', component: MapComponent},
  {path: 'chat', component: ChatContainerComponent},
  {path: 'admin/drivers', component: DriversComponent},
  {path: 'profile', component: ProfileContainerComponent},
  {path: 'rides', component: RidesHistoryComponent},
  {path: 'verify/:verificationCode', component: VerifyRegistrationComponent},
  {path: '**', component: NotFoundPageComponent},
  // {canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
