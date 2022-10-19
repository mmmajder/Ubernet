import {Injectable} from '@angular/core';

var SockJS = require('sockjs-client');
var Stomp = require('stompjs');

@Injectable({
  providedIn: 'root'
})
export class NotificationsService {
  stompClient: any;

  activityId: any;
  text: any;
  messages: Array<String> = new Array<String>();

  constructor() {
  }

  send() {
    this.stompClient.send('/app/hello/' + this.activityId, {}, JSON.stringify({'name': this.text}));
  }

  connect() {
    const that = this;
    const socket = new SockJS('tst-rest.mypageexample/hello?activityId=' + this.activityId);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, function (frame: string) {
      console.log('Connected: ' + frame);
      that.stompClient.subscribe('/topic/greetings/' + that.activityId, function (greeting: { body: string; }) {
        that.messages.push(JSON.parse(greeting.body).content);
      });
    }, function (err: any) {
      console.log('err', err);
    });
  }

}
