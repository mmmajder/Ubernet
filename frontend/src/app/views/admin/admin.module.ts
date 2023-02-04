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
import {UsersContainerComponent} from "./container/users-container/users-container.component";
import {ChatContainerComponent} from './container/chat-container/chat-container.component';
import {ListOfChatsComponent} from './components/list-of-chats/list-of-chats.component';
import {AdminChatComponent} from './components/admin-chat/admin-chat.component';
import {MatListModule} from "@angular/material/list";
import {MatTooltipModule} from "@angular/material/tooltip";
import {DriversComponent} from './components/drivers/drivers.component';
import {MatTabsModule} from "@angular/material/tabs";
import {AdminProfileContainerComponent} from './container/admin-profile-container/admin-profile-container.component';
import {
  RegisterNewDriverDialogComponent
} from './components/register-new-driver-dialog/register-new-driver-dialog.component';
import {MatStepperModule} from "@angular/material/stepper";
import {DriversProfileDialogComponent} from './components/drivers-profile-dialog/drivers-profile-dialog.component';
import {ChangesRequestDialogComponent} from './components/changes-request-dialog/changes-request-dialog.component';
import {AdminCommentsComponent} from './components/admin-comments/admin-comments.component';
import {CustomersComponent} from './components/customers/customers.component';
import {
  CustomersProfileDialogComponent
} from './components/customers-profile-dialog/customers-profile-dialog.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatSelectModule} from "@angular/material/select";

@NgModule({
  declarations: [
    UsersContainerComponent,
    ChatContainerComponent,
    ListOfChatsComponent,
    AdminChatComponent,
    DriversComponent,
    AdminProfileContainerComponent,
    RegisterNewDriverDialogComponent,
    DriversProfileDialogComponent,
    ChangesRequestDialogComponent,
    AdminCommentsComponent,
    CustomersComponent,
    CustomersProfileDialogComponent,
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
    MatStepperModule,
    MatCheckboxModule,
    MatSelectModule
  ],
  exports: [
    UsersContainerComponent,
    AdminProfileContainerComponent,
  ]
})
export class AdminModule {
}
