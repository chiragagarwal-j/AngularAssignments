import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { VendingService } from '../vending.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  title = 'vending-machine';
  products: any[] = [];

  constructor(private vendingService: VendingService, private router: Router) {
    this.products = this.vendingService.getProducts();
  }

  navigateToSection(productId: number) {
    if (productId) {
      let section: number;
      if (productId >= 1 && productId <= 5) {
        section = 1;
      } else if (productId >= 6 && productId <= 10) {
        section = 2;
      } else if (productId >= 11 && productId <= 15) {
        section = 3;
      } else {
        section = 1;
      }
      this.router.navigate(['/section', section]);
    }
  }
}
