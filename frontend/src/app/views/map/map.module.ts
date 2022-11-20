import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatTableModule} from "@angular/material/table";
import {MatCardModule} from "@angular/material/card";
import {MatSortModule} from "@angular/material/sort";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatDialogModule} from "@angular/material/dialog";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {SharedModule} from "../../shared/shared.module";
import {SearchDirectionsCustomerComponent} from "./components/search-directions-customer/search-directions-customer.component";
import {MapComponent} from "./container/map/map.component";
import { SearchDirectionsUnauthenticatedComponent } from './components/search-directions-unauthenticated/search-directions-unauthenticated.component';
import {MatListModule} from "@angular/material/list";
import { MapUnauthenticatedComponent } from './components/map-unauthenticated/map-unauthenticated.component';

@NgModule({
  declarations: [
    SearchDirectionsCustomerComponent,
    MapComponent,
    SearchDirectionsUnauthenticatedComponent,
    MapUnauthenticatedComponent,
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
        SharedModule,
        MatListModule,
    ],
    exports: [
        MapComponent,
    ],
  bootstrap: []
})
export class MapModule {
}
