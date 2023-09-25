import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class VendingService {
  private products: any[] = [
    { id: 1, name: 'Product A', quantity: 5, price: 10 },
    { id: 2, name: 'Product B', quantity: 5, price: 10 },
    { id: 3, name: 'Product C', quantity: 5, price: 10 },
    { id: 4, name: 'Product D', quantity: 5, price: 10 },
    { id: 5, name: 'Product E', quantity: 5, price: 10 },

    { id: 6, name: 'Product F', quantity: 5, price: 10 },
    { id: 7, name: 'Product G', quantity: 5, price: 10 },
    { id: 8, name: 'Product H', quantity: 5, price: 10 },
    { id: 9, name: 'Product I', quantity: 5, price: 10 },
    { id: 10, name: 'Product J', quantity: 5, price: 10 },

    { id: 11, name: 'Product K', quantity: 5, price: 10 },
    { id: 12, name: 'Product L', quantity: 5, price: 10 },
    { id: 13, name: 'Product M', quantity: 5, price: 10 },
    { id: 14, name: 'Product N', quantity: 5, price: 10 },
    { id: 15, name: 'Product O', quantity: 5, price: 10 },
  ];

  getProducts() {
    return this.products;
  }
  constructor() { }
}
