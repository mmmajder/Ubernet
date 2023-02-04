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
import {
  SearchDirectionsCustomerComponent
} from "./components/search-directions-customer/search-directions-customer.component";
import {MapComponent} from "./container/map/map.component";
import {
  SearchDirectionsUnauthenticatedComponent
} from './components/search-directions-unauthenticated/search-directions-unauthenticated.component';
import {MatListModule} from "@angular/material/list";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatSelectModule} from "@angular/material/select";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatStepperModule} from "@angular/material/stepper";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {NgxMaterialTimepickerModule} from "ngx-material-timepicker";
import {MatRadioModule} from "@angular/material/radio";
import {NotificationDriverComponent} from './components/notification-driver/notification-driver.component';
import {
  ReasonForRideCancellationComponent
} from "./components/reason-for-ride-cancelation/reason-for-ride-cancellation.component";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatChipsModule} from "@angular/material/chips";
import {CustomerModule} from "../customer/customer.module";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

@NgModule({
  declarations: [
    SearchDirectionsCustomerComponent,
    MapComponent,
    SearchDirectionsUnauthenticatedComponent,
    NotificationDriverComponent,
    ReasonForRideCancellationComponent,
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
    MatExpansionModule,
    MatSelectModule,
    MatSidenavModule,
    MatStepperModule,
    MatCheckboxModule,
    NgxMaterialTimepickerModule,
    MatRadioModule,
    MatAutocompleteModule,
    MatChipsModule,
    CustomerModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
  ],
  exports: [
    MapComponent,
  ],
  bootstrap: []
})
export class MapModule {
}
