import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomepageContainerComponent} from "./views/homepage/container/homepage-container/homepage-container.component";
import {DriverContainerComponent} from "./views/driver/container/driver-container/driver-container.component";
import {NotFoundPageComponent} from "./views/404/not-found-page/not-found-page.component";
import {CustomerContainerComponent} from "./views/customer/container/customer-container/customer-container.component";
import {AdminContainerComponent} from "./views/admin/container/admin-container/admin-container.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'driver', component: DriverContainerComponent},
  {path: 'admin', component: AdminContainerComponent},
  {path: 'customer', component: CustomerContainerComponent},
  { path: '**', component: NotFoundPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
