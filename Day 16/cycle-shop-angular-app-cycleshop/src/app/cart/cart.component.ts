import { Component, OnInit } from '@angular/core';
import { CyclesService } from '../service/cycles.service';
import { CartItem } from '../models/CartItems';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];

  constructor(private cyclesService: CyclesService) {}

  ngOnInit(): void {
    this.getCartItems();
  }

  getCartItems(): void {
    this.cyclesService.getCartItems().subscribe((cartItems) => {
      this.cartItems = cartItems;
    });
  }

  quantityOptions: number[] = Array.from({ length: 10 }, (_, i) => i + 1);

}
