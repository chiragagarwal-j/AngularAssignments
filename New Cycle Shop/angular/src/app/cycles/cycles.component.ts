import { Component } from '@angular/core';
import { Cycle } from '../cycle';
import { ApiService } from '../api.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-cycles',
  templateUrl: './cycles.component.html',
  styleUrls: ['./cycles.component.css']
})
export class CyclesComponent {
  data: Cycle[] = []; // Use the Cycle class for data

  constructor(private api: ApiService,public authService: AuthService) { }

  ngOnInit() {
    this.loadData();
  }

  private loadData() {
    this.api.getData().subscribe(res => this.data = res);
  }

  borrowCycle(cycleId: number) {
    this.api.borrowCycle(cycleId).subscribe(res => this.data = res);
  }

  returnCycle(cycleId: number) {
    this.api.returnCycle(cycleId).subscribe(res => this.data = res);
  }

  restockCycle(cycleId: number, quantity: number) {
    this.api.restockCycle(cycleId, quantity).subscribe(res => this.data = res);
  }
}
