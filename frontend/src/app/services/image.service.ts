import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

export class EncodedImage {
  data: string;
}

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

  public postProfileImage(email: string, file: any): Observable<EncodedImage> {
    const formData = new FormData();
    formData.append("file", file);
    return this.http.post<EncodedImage>(this.imageUrl + "/" + email, formData, this.httpOptions);
  }

  public getProfileImage(email: string): Observable<EncodedImage> {
    return this.http.get<EncodedImage>(this.imageUrl + "/" + email, this.httpOptions);
  }
}
