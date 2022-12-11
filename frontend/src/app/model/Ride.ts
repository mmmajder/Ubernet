import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {BehaviorSubject, catchError, finalize, Observable, of} from "rxjs";
import {RidesHistoryService} from "../services/rides-history.service";

export class Ride {

}

export class RidesDataSource implements DataSource<Ride> {

  private ridesSubject = new BehaviorSubject<Ride[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  public loading$ = this.loadingSubject.asObservable();

  constructor(private ridesService: RidesHistoryService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Ride[]> {
    return this.ridesSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.ridesSubject.complete();
    this.loadingSubject.complete();
  }

  loadRides(filter = '', driverEmail = '', customerEmail = '',
              sortDirection = 'asc', pageIndex = 0, pageSize = 3) {

    this.loadingSubject.next(true);

    this.ridesService.getRides(filter, driverEmail, customerEmail, sortDirection,
      pageIndex, pageSize).pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe(rides => this.ridesSubject.next(rides));
  }
}
