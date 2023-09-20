import { Component, OnInit } from '@angular/core';
import { CyclesService } from '../service/cycles.service';
import { Cycle } from '../models/cycle';
import { AuthService } from '../service/auth.service';
import { faCartShopping, faArrowUp, faArrowDown, faTachographDigital } from '@fortawesome/free-solid-svg-icons';
import { ConcreteCycle } from '../models/ConcreteCycle';
@Component({
  selector: 'app-cycles',
  templateUrl: './cycles.component.html',
  styleUrls: ['./cycles.component.css']
})

export class CyclesComponent implements OnInit {

  cycles: Cycle[] = [];
  cyc: ConcreteCycle;
  quantityValue: number = 0;
  faCartShopping = faCartShopping;
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  quantityOptions: number[] = Array.from({ length: 10 }, (_, i) => i + 1);

  constructor(private cyclesService: CyclesService, public authService: AuthService) {
    this.cyc = new ConcreteCycle();
  }

  ngOnInit(): void {
    this.getCycles();
  }

  getCycles(): void {
    this.cyclesService.getCycles()
      .subscribe(cycles => this.cycles = cycles);
  }

  parseQuantity(qtyString: string): number {
    return parseInt(qtyString, 10);
  }

  addToCart(id: number, quantity: number) {
    this.cyclesService.addToCart(id, quantity)
      .subscribe(cycle => {
        console.log('Added to cart:', cycle);
      });
  }

  borrow(id: number): void {
    this.cyclesService.borrowCycle(id)
      .subscribe(cycle => {
        this.cycles = this.cycles.map(c => { return c.id === cycle.id ? cycle : c }) as Cycle[];
      });
  }

  return(id: number): void {
    this.cyclesService.returnCycle(id)
      .subscribe(cycle => {
        this.cycles = this.cycles.map(c => { return c.id === cycle.id ? cycle : c }) as Cycle[];
      });
  }

  restock(id: number, quantity: number): void {
    console.log(id, quantity);
    this.cyclesService.restockCycle(id, quantity)
      .subscribe(cycle => {
        this.cycles = this.cycles.map(c => { return c.id === cycle.id ? cycle : c }) as Cycle[];
      });
  }
}
