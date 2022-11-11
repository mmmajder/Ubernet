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
import {AdminContainerComponent} from "./container/admin-container/admin-container.component";
import {ChatContainerComponent} from './container/chat-container/chat-container.component';
import {ListOfChatsComponent} from './components/list-of-chats/list-of-chats.component';
import {AdminChatComponent} from './components/admin-chat/admin-chat.component';
import {MatListModule} from "@angular/material/list";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DriverRegistartionComponent} from './components/driver-registartion/driver-registartion.component';
import {DriversComponent} from './container/drivers/drivers.component';
import {MatTabsModule} from "@angular/material/tabs";
import {DriversListComponent} from './components/drivers-list/drivers-list.component';

@NgModule({
  declarations: [
    AdminContainerComponent,
    ChatContainerComponent,
    ListOfChatsComponent,
    AdminChatComponent,
    DriverRegistartionComponent,
    DriversComponent,
    DriversListComponent,
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
    MatTooltipModule,
    MatTabsModule,
  ],
  exports: [
    AdminContainerComponent,
  ]
})
export class AdminModule {
}
