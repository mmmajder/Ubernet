import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private readonly imageUrl: string;

  httpOptions = {
    headers: new HttpHeaders({
      'Access-Control-Allow-Origin': '*',
      'Authorization': 'authkey',
    })
  };

  constructor(private http: HttpClient) {
    this.imageUrl = 'http://localhost:8000/image';
  }

  public postProfileImage(email: string, file: any) {
    // TODO get rid of 'any'
    let formData = new FormData();
    formData.append("file", file);

    return this.http.post<Object>(this.imageUrl + "/" + email, formData, this.httpOptions); // do not change for AuthService.getHttpOptions() because of 'Content-Type' header
  }

  public getProfileImage(email: string) {
    console.log("getProfileImage service")
    return this.http.get(this.imageUrl + "/" + email, this.httpOptions);
  }
}
