import {RideDetails} from "./RideDetails";

export class DriverNotification {
  id!: number;
  ride!: RideDetails;
  driverNotificationType!: string;
  isFinished!: boolean;
}
