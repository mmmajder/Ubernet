import {RideDetails} from "./RideDetails";

export class ReasonDialogDTO {
  ride: RideDetails
  shouldSetDriverInactive: boolean

  constructor(ride: RideDetails, shouldSetDriverInactive: boolean) {
    this.ride = ride;
    this.shouldSetDriverInactive = shouldSetDriverInactive;
  }
}
