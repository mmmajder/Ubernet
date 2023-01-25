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
import {DashboardContainerComponent} from "./dashboard-container/dashboard-container.component";
import {AdminModule} from "../admin/admin.module";
import {CustomerModule} from "../customer/customer.module";
import {DriverModule} from "../driver/driver.module";
import {ProfileContainerComponent} from './profile-container/profile-container.component';
import {RidesHistoryContainerComponent} from './rides-history-container/rides-history-container.component';
import {MatSelectModule} from "@angular/material/select";
import {AnalyticsContainerComponent} from './analytics-container/analytics-container.component';
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";

@NgModule({
  declarations: [
    DashboardContainerComponent,
    ProfileContainerComponent,
    RidesHistoryContainerComponent,
    AnalyticsContainerComponent
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
    AdminModule,
    CustomerModule,
    DriverModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  exports: [
    DashboardContainerComponent
  ],
  bootstrap: []
})
export class PagesModule {
}
