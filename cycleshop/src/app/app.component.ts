import { Component, OnInit } from '@angular/core';
import { ApiService } from './api.service';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'cycleshop';
  data: any[] = [];

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.loadData();
  }

  private loadData() {
    this.api.getData().subscribe(
      (response: any) => {
        console.log(response);
        this.data = response;
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );
  }

  borrowCycle(cycleId: number) {
    this.api.fetchDataAfterAction(() => this.api.borrowCycle(cycleId)).subscribe(
      (response: any) => {
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
      (response: any) => {
        console.log(response);
        this.data = response;
      },
      (error) => {
        console.error('Error returning cycle:', error);
      }
    );
  }
}
