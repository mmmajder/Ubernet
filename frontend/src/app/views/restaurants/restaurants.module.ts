import {NgModule} from "@angular/core";
import {RestaurantsContainerComponent} from "./container/restaurants-container.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatTableModule} from "@angular/material/table";
import {MatSortModule} from "@angular/material/sort";
import {RouterModule} from "@angular/router";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatInputModule} from "@angular/material/input";
import {RestaurantTableComponent} from "./components/restaurant-table/restaurant-table.component";
import {MatIconModule} from "@angular/material/icon";
import {
  CreateRestaurantDialogComponent
} from "./components/create-restaurant-dialog/create-restaurant-dialog.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatDialogModule} from "@angular/material/dialog";
import {CommonModule} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {SharedModule} from "../../shared/shared.module";

@NgModule({
  declarations: [
    RestaurantsContainerComponent,
    RestaurantTableComponent,
    CreateRestaurantDialogComponent
  ],
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatFormFieldModule,
    MatTableModule,
    MatSortModule,
    RouterModule,
    MatPaginatorModule,
    MatInputModule,
    MatIconModule,
    FormsModule,
    MatDialogModule,
    ReactiveFormsModule,
    CommonModule,
    MatSnackBarModule,
    MatMenuModule,
    SharedModule
  ],
  exports: [
    RestaurantsContainerComponent,
  ],
  bootstrap: [RestaurantsContainerComponent]
})
export class RestaurantsModule {
}
