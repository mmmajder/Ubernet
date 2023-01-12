import {Component, OnInit} from '@angular/core';
import {ICreateOrderRequest, IPayPalConfig} from "ngx-paypal";
import {IAddressPortable, IPartyName, IPhone, ITaxInfo} from "ngx-paypal/lib/models/paypal-models";
import * as $ from 'jquery'
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Inject} from '@angular/core';
import {Ride} from "../../../../model/Ride";
import {RideService} from "../../../../services/ride.service";
import {RideCreate} from "../../../../model/RideCreate";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  public payPalConfig ?: IPayPalConfig;
  defaultCardNumber: string;
  private rideService: any;
  private ride: RideCreate;

  constructor(rideService: RideService, @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<PaymentComponent>) {
    console.log("Miki")
    console.log(data)
    this.ride = data.dataKey
    this.rideService = rideService
  }

  ngOnInit(): void {
    this.defaultCardNumber = "1234123412341234"
    this.initConfig();
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'USD',
      clientId: 'sb',
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        payer: {
          email_address: 'miki@gmail.com',
          name: {
            given_name: "Milan",
            surname: "Ajder",
          },
          address: {
            address_line_1: "2211 N First Street",
            address_line_2: "Building 17",
            admin_area_2: "San Jose",
            admin_area_1: "CA",
            postal_code: "95131",
            country_code: "US"
          },
          payer_id: "QYR5Z8XDVJNXQ",
        },
        advanced: {
          commit: 'true',
          extraQueryParams: [
            {
              name: 'intent',
              value: 'authorize'
            }
          ]
        },
        funding_instruments: [
          {
            credit_card: {
              number: '5555555555554444',
              type: 'mastercard',
              expire_month: '12',
              expire_year: '2025',
              cvv2: '123',
              first_name: 'John',
              last_name: 'Doe',
            }
          }
        ],
        purchase_units: [
          {
            amount: {
              currency_code: 'USD',
              value: '9.99',
              breakdown: {
                item_total: {
                  currency_code: 'USD',
                  value: '9.99'
                }
              }
            },
            items: [
              {
                name: 'Enterprise Subscription',
                quantity: '1',
                category: 'DIGITAL_GOODS',
                unit_amount: {
                  currency_code: 'USD',
                  value: '9.99',
                },
              }
            ]
          }
        ]
      },
      advanced: {
        commit: 'true',
      },
      style: {
        label: 'paypal',
        layout: 'vertical'
      },
      onApprove: (data, actions) => {
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then((details: any) => {
          console.log('onApprove - you can get full order details inside onApprove: ', details);
        });
      },
      onClientAuthorization: (data) => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
        console.log(this.ride)
        this.rideService.createRide(this.ride).subscribe((res: Ride) => {
          console.log(res)
          this.dialogRef.close();
        })
        // this.showSuccess = true;
      },
      onCancel: (data, actions) => {
        console.log('OnCancel', data, actions);
      },
      onError: err => {
        console.log('OnError', err);
      },
      onClick: async (data, actions) => {
        // const delay = (ms: number) => {
        //   return new Promise(resolve => setTimeout(resolve, ms));
        // }
        console.log('onClick', data, actions);
        // await delay(5000);
        // console.log("stigao sam")
        // $("#credit-card-number").val("1234123413241234")
        // $("#request").on("click", () => {
        //   console.log("Kliknuo sam")
        // })
      },
    };
  }


}
