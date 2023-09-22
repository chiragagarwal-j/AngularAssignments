import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Cycle } from '../models/cycle';
import { Observable } from 'rxjs';
import { CartItem } from '../models/CartItems';


@Injectable({
  providedIn: 'root'
})
export class CyclesService {
  baseUrl = 'http://localhost:8080/api/cycles';

  constructor(private http: HttpClient) { }

  getCycles(): Observable<Cycle[]> {
    return this.http.get<Cycle[]>(`${this.baseUrl}/list`);
  }

  borrowCycle(id: number): Observable<Cycle> {
    return this.http.post<Cycle>(`${this.baseUrl}/${id}/borrow`, {});
  }

  returnCycle(id: number): Observable<Cycle> {
    return this.http.post<Cycle>(`${this.baseUrl}/${id}/return`, {});
  }

  restockCycle(id: number, quantity: number): Observable<Cycle> {
    const params = new HttpParams().set('quantity', quantity.toString());
    return this.http.post<Cycle>(`${this.baseUrl}/${id}/restock`, {}, { params: params });
  }

  addToCart(id: number, quantity: number): Observable<Cycle> {
    const body = {
      id: id,
      quantity: quantity
    };

    return this.http.post<Cycle>(`${this.baseUrl}/addToCart`, body);
  }

  getCartItems(): Observable<CartItem[]> {
    return this.http.get<CartItem[]>(`${this.baseUrl}/cart`);
  }

}