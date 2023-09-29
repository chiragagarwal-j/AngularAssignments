import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})

export class UserService {
    constructor(private http: HttpClient) { }
    getUsers(): Observable<User[]> {
        return this.http.get<User[]>('http://localhost:8080/forum/getUsers');
    }
}

export interface User {
    id: number;
    name: string;
    password: string;
}