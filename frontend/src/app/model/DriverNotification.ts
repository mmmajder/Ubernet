import {Ride} from "./Ride";

export class DriverNotification {
  id!: number;
  ride!: Ride;
  driverNotificationType!: string;
  isFinished!: boolean;
}
