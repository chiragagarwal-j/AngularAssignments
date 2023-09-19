import { Component } from '@angular/core';
import { Cycle } from '../cycle';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-cycles',
  templateUrl: './cycles.component.html',
  styleUrls: ['./cycles.component.css']
})
export class CyclesComponent {
  data: Cycle[] = []; // Use the Cycle class for data

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.loadData();
  }

  private loadData() {
    this.api.getData().subscribe(
      (response: Cycle[]) => { // Specify the correct data type for response
        console.log(response);
        this.data = response;
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );
  }

  borrowCycle(cycleId: number ) {
    this.api.fetchDataAfterAction(() => this.api.borrowCycle(cycleId)).subscribe(
      (response: Cycle[]) => { // Specify the correct data type for response
        console.log(response);
        this.data = response;
      },
      (error) => {
        console.error('Error borrowing cycle:', error);
      }
    );
  }

  returnCycle(cycleId: number) {
    this.api.fetchDataAfterAction(() => this.api.returnCycle(cycleId)).subscribe(
      (response: Cycle[]) => { // Specify the correct data type for response
        console.log(response);
        this.data = response;
      },
      (error) => {
        console.error('Error returning cycle:', error);
      }
    );
  }
}
