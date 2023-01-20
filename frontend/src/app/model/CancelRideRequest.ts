import {RideDetails} from "./RideDetails";

export class CancelRideRequest {
  shouldSetDriverInactive: boolean;
  reason: string;
}
