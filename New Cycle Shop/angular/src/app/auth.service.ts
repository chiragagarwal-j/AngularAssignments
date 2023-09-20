import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginForm } from './login-form';
import { User } from './User';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8080/api/auth';
  private _isLoggedIn$: boolean = false;
  private _payload: Record<string, any> = {};
  constructor(private http: HttpClient, private router: Router) { 
    this._isLoggedIn$ = !!localStorage.getItem('token');
    if (this._isLoggedIn$) {
      const token = localStorage.getItem('token');
      const payload = token?.split('.')[1];
      this._payload = JSON.parse(atob(payload as string));
      console.log(this._payload);
    }
  }

  get isLoggedIn(): boolean {
    return this._isLoggedIn$;
  }

  login(userCred: LoginForm): Observable<User> {
    return this.http.post<User>(`${this.authUrl}/token`, userCred).pipe(
      tap(res => {
        this._isLoggedIn$ = true;
        localStorage.setItem('token', res.token);
        this.router.navigate(['/cycles']);
      })
    );
  }
  get token(): string | null {
    return localStorage.getItem('token')
  }

  logout(): void {
    localStorage.removeItem('token');
    this._isLoggedIn$ = false;
  }

  isUserAdmin(): boolean {
    if (!this._isLoggedIn$) return false;
    return this._payload['role'] === 'ROLE_ADMIN';
  }

  getUsername(): string {
    if (!this._isLoggedIn$) return 'anonymous';
    return this._payload['sub'];
  }

  isExpired(): boolean {
    console.log(this._payload);
    const {exp} = this._payload;
    return Date.now()  >= exp * 1000;
  }
}
