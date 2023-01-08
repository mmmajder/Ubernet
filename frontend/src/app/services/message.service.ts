import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Message} from "../model/Message";
import { Chat } from '../model/Chat';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private readonly messageUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.messageUrl = 'http://localhost:8000/messages';
  }

  public getMessagesForClientEmail(clientEmail:string): Observable<Message[]>{
    return this.http.get<Message[]>(this.messageUrl + "/" + clientEmail, this.httpOptions);
  }

  public getChats(): Observable<Chat[]>{
    return this.http.get<Chat[]>(this.messageUrl + "/chats", this.httpOptions);
  }

  // // for admin
  // public getMessagesAsAdmin(clientEmail:string){
  //   return this.http.get(this.messageUrl + "/admin/" + clientEmail, this.httpOptions);
  // }
  //
  // public getTest() {
  //   return this.http.get(this.messageUrl + "/test" , this.httpOptions);
  // }

  // for admin

}
