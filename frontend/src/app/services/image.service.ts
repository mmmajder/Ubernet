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

  public postProfileImage(email: string, file: any){
    // TODO get rid of 'any'
    let formData = new FormData();
    formData.append("file", file);

    return this.http.post<Object>(this.imageUrl + "/" + email, formData, AuthService.getHttpOptions());
  }

  public getProfileImage(email: string){
    return this.http.get(this.imageUrl + "/" + email, this.httpOptions);
  }
}
