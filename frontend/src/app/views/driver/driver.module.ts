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
import {ProfileDriverContainerComponent} from './container/profile-driver-container/profile-driver-container.component';
import {MatTabsModule} from "@angular/material/tabs";
import {MatListModule} from "@angular/material/list";
import {CustomerModule} from "../customer/customer.module";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {CarSettingsComponent} from './components/car-settings/car-settings.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSelectModule} from "@angular/material/select";
import {DriverRequestChangeComponent} from "./components/driver-request-change/driver-request-change.component";

@NgModule({
  declarations: [
    ProfileDriverContainerComponent,
    CarSettingsComponent,
    DriverRequestChangeComponent,
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
        MatTabsModule,
        MatListModule,
        CustomerModule,
        MatSlideToggleModule,
        MatCheckboxModule,
        MatSelectModule,
    ],
  exports: [
    ProfileDriverContainerComponent
  ]
})
export class DriverModule {
}
