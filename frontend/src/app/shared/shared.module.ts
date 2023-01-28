import {NgModule} from "@angular/core";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatMenuModule} from "@angular/material/menu";
import {CommonModule} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {SidenavComponent} from './sidenav/sidenav/sidenav.component';
import {MatBadgeModule} from "@angular/material/badge";
import {UserChatComponent} from "./user-chat/user-chat.component";
import {MatListModule} from "@angular/material/list";
import {MessageComponent} from "./message/message.component";
import {NewMessageComponent} from "./new-message/new-message.component";
import {MatTooltipModule} from "@angular/material/tooltip";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {ProfileDataComponent} from "./profile-edit/profile-data/profile-data.component";
import {ChangePasswordComponent} from "./profile-edit/change-password/change-password.component";
import {MatCardModule} from "@angular/material/card";
import {StarRatingComponent} from './star-rating/star-rating.component';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {ReviewsComponent} from './reviews/reviews.component';
import {RidesHistoryComponent} from "./rides-history/rides-history.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {RideDetailsDialogComponent} from './ride-details-dialog/ride-details-dialog.component';
import {MatChipsModule} from "@angular/material/chips";
import {MatExpansionModule} from "@angular/material/expansion";
import {NotificationsComponent} from "./notification/notifications.component";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";

@NgModule({
  declarations: [SidenavComponent, UserChatComponent, MessageComponent, NewMessageComponent, RidesHistoryComponent, ProfileDataComponent, ChangePasswordComponent, StarRatingComponent, UserProfileComponent, ReviewsComponent, RideDetailsDialogComponent, NotificationsComponent],
  imports: [
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    CommonModule,
    MatButtonModule,
    MatBadgeModule,
    MatListModule,
    MatTooltipModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatChipsModule,
    MatExpansionModule,
    MatSlideToggleModule,
  ],
  exports: [SidenavComponent, MessageComponent, NewMessageComponent, RidesHistoryComponent, ProfileDataComponent, ChangePasswordComponent, StarRatingComponent, UserProfileComponent, ReviewsComponent],
})
export class SharedModule {
}
