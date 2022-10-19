import {Injectable} from "@angular/core";
import {ComponentStore, tapResponse} from "@ngrx/component-store";
import {exhaustMap, Observable, tap} from "rxjs";
import {MyNotification} from "../navbar/navbar/navbar.component";
import {NavbarService, NotificationDto} from "../../services/navbar.service";

export interface NavbarState {
  notifications: NotificationDto[];
  colorBell: boolean;
}

const INITIAL_STATE: NavbarState = {
  notifications: [],
  colorBell: false
}

@Injectable()
export class NavbarStore extends ComponentStore<NavbarState> {

  constructor(private navbarService: NavbarService) {
    super(INITIAL_STATE);
  }

  readonly notifications$: Observable<NotificationDto[]> = this.select(state => state.notifications);
  readonly colorBell$: Observable<boolean> = this.select(state => state.colorBell);

  getNotifications = this.effect(trigger$ => trigger$
    .pipe(
      exhaustMap(_ => {
        return this.navbarService.findAll()
          .pipe(tapResponse(
            (response) => this.patchState({notifications: response}),
            (error) => console.error(error),
          ))
      })
    )
  )

  createNotification = this.effect(
    (not$: Observable<MyNotification>) => {
      return not$.pipe(
        exhaustMap((notification) =>
          this.navbarService.createNotification(notification)
            .pipe(tap({
              next: (response) => {
                this.addNotification(response);
                this.colorNotification(notification.colorBell);
              },
              error: (error) => console.error(error),
            }))
        )
      )
    });

  readonly addNotification = this.updater((state, notification: NotificationDto) => ({
    ...state, notifications: [notification, ...state.notifications]
  }));

  readonly colorNotification = this.updater((state, color: boolean) => ({
    ...state, colorBell: color,
  }));

  openedNotification() {
    this.colorNotification(false);
  }
}
