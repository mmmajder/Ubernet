import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";
import {Comment} from "../model/Review";

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private readonly commentsUrl: string;

  constructor(private http: HttpClient) {
    this.commentsUrl = 'http://localhost:8000/comments';
  }

  public getComments(userEmail: string): Observable<Comment[]> {
    console.log(userEmail)
    return this.http.get<Comment[]>(this.commentsUrl + "/get-comments/" + userEmail, AuthService.getHttpOptions());
  }

  public addComments(userEmail: string, adminEmail: string, content: string): Observable<Object> {
    const body = {
      'userEmail': userEmail,
      'adminEmail': adminEmail,
      'content': content
    }
    return this.http.post<Object>(this.commentsUrl + "/add-comment", body, AuthService.getHttpOptions());
  }

}
