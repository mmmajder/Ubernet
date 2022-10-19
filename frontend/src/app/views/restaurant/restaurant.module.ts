import {NgModule} from "@angular/core";
import {RestaurantContainerComponent} from "./container/restaurant-container/restaurant-container.component";
import {RestaurantDetailsComponent} from "./components/restaurant-details/restaurant-details.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {MatCardModule} from "@angular/material/card";
import {MatSortModule} from "@angular/material/sort";
import {CreateMenuItemDialogComponent} from "./components/create-menu-item-dialog/create-menu-item-dialog.component";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {SharedModule} from "../../shared/shared.module";

@NgModule({
  declarations: [
    RestaurantContainerComponent,
    RestaurantDetailsComponent,
    CreateMenuItemDialogComponent
  ],
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatCardModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    FormsModule,
    CommonModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    MatMenuModule,
    SharedModule
  ],
  exports: [
    RestaurantContainerComponent
  ],
  bootstrap: [RestaurantContainerComponent]
})
export class RestaurantModule {
}
