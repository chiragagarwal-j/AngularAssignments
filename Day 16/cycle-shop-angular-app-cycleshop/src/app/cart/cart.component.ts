import { Component, OnInit } from '@angular/core';
import { CyclesService } from '../service/cycles.service';
import { CartItem } from '../models/CartItems';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog'; // Import MatDialog
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component'; // Import the ConfirmationDialogComponent

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];

  constructor(
    private cyclesService: CyclesService,
    private router: Router,
    private dialog: MatDialog // Inject MatDialog
  ) {}

  ngOnInit(): void {
    this.getCartItems();
  }

  getCartItems(): void {
    this.cyclesService.getCartItems().subscribe((cartItems) => {
      this.cartItems = cartItems;
    });
  }

  quantityOptions: number[] = Array.from({ length: 10 }, (_, i) => i + 1);

  calculateTotalPrice(): number {
    let totalPrice = 0;
    for (const item of this.cartItems) {
      totalPrice += item.price * item.quantity;
    }
    return totalPrice;
  }

  goToCheckout(): void {
    // Open the confirmation dialog
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px', // Set your desired width
      height: '200px', // Set your desired height
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        // User confirmed, proceed with checkout
        this.router.navigate(['/checkout']);
      }
      // Otherwise, do nothing (user canceled)
    });
  }
}
