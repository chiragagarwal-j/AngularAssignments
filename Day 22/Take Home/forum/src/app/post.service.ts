import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = 'http://localhost:8080/forum';

  constructor(private http: HttpClient) { }

  getPost(postId: number) {
    return this.http.get(`${this.baseUrl}/post/${postId}`);
  }

  addComment(postId: number, commentData: any) {
    const url = `${this.baseUrl}/post/${postId}/add-comment`;
    return this.http.post(url, commentData);
  }

  getCommentsForPost(postId: number): Observable<any[]> {
    const url = `${this.baseUrl}/post/${postId}/comments`;
    return this.http.get<any[]>(url);
  }

  getAllUsers(): Observable<any[]> {
    const url = `${this.baseUrl}/getUsers`;
    return this.http.get<any[]>(url);
  }

}