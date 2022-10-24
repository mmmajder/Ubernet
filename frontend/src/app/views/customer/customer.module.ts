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
import {CustomerContainerComponent} from "./container/customer-container/customer-container.component";
import { FavoritesComponent } from './components/favorites/favorites/favorites.component';
import {MatListModule} from "@angular/material/list";
import { ActivityLogComponent } from './components/activity-log/activity-log/activity-log.component';

@NgModule({
  declarations: [
    CustomerContainerComponent,
    FavoritesComponent,
    ActivityLogComponent
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
    CustomerContainerComponent
  ]
})
export class CustomerModule {
}
