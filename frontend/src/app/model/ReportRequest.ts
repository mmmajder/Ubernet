export class ReportRequest {
  email: string | undefined;
  startDate: string;
  endDate: string;
}

export class ReportResponse {
  numberOfRides: number[];
  numberOfKm: number[];
  money: number[];
  averageMoneyPerDay: number;
  totalSum: number;
}
