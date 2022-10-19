import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {
  RestaurantContainerComponent
} from "./views/restaurant/container/restaurant-container/restaurant-container.component";
import {HomepageContainerComponent} from "./views/homepage/container/homepage-container/homepage-container.component";

const routes: Routes = [
  {path: '', component: HomepageContainerComponent},
  {path: 'restaurant/:id', component: RestaurantContainerComponent}
  // 404 - { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
