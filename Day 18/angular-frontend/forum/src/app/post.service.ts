import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = 'http://localhost:8080/forum';

  constructor(private http: HttpClient) { }

  getPost(postId: number) {
    return this.http.get(`${this.baseUrl}/post/${postId}`);
  }
}