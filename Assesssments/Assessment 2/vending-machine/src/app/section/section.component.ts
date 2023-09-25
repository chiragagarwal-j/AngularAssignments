import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VendingService } from '../vending.service';

@Component({
  selector: 'app-section',
  templateUrl: './section.component.html',
  styleUrls: ['./section.component.css']
})
export class SectionComponent {
  section: number = 1;
  sectionProducts: any[] = [];
  selectedProducts: any[] = [];
  totalAmount: number = 0;

  constructor(private route: ActivatedRoute, private vendingService: VendingService,private router: Router) {
    this.route.params.subscribe(params => {
      this.section = +params['section'];
      this.filterProductsBySection();
    });
  }

  filterProductsBySection() {
    this.sectionProducts = this.vendingService.getProducts()
      .filter(product => {
        if (this.section === 1 && product.id >= 1 && product.id <= 5) {
          return true;
        } else if (this.section === 2 && product.id >= 6 && product.id <= 10) {
          return true;
        } else if (this.section === 3 && product.id >= 11 && product.id <= 15) {
          return true;
        }
        return false;
      });

    
    this.sectionProducts.forEach(product => {
      product.selectedQuantity = 1;
    });
  }

  addToCart(product: any) {
    this.selectedProducts.push(product);
    this.updateTotalAmount();
  }

  removeFromCart(product: any) {
    const index = this.selectedProducts.indexOf(product);
    if (index !== -1) {
      this.selectedProducts.splice(index, 1);
      this.updateTotalAmount();
    }
  }

  
  updateTotalAmount() {
    this.totalAmount = this.selectedProducts.reduce((total, product) => {
      return total + product.price * product.selectedQuantity;
    }, 0);
  }

  
  generateQuantityOptions(availableQuantity: number): number[] {
    const options = [];
    for (let i = 1; i <= availableQuantity; i++) {
      options.push(i);
    }
    return options;
  }

  
  checkout() {
    for (const product of this.selectedProducts) {
      product.quantity -= product.selectedQuantity;
    }

    
    this.selectedProducts = [];
    this.updateTotalAmount();
    this.router.navigate(['/home']);
  }
}
