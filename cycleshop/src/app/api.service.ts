import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class ApiService {
	private baseUrl = 'http://localhost:8080/api/cycleStocks';

	constructor(private http: HttpClient) { }

	getData(): Observable<any[]> {
		return this.http.get<any[]>(this.baseUrl);
	}

	borrowCycle(cycleId: number): Observable<any[]> {
		const borrowUrl = `${this.baseUrl}/${cycleId}/borrow`;
		return this.http.post<any[]>(borrowUrl, null);
	}

	returnCycle(cycleId: number): Observable<any[]> {
		const returnUrl = `${this.baseUrl}/${cycleId}/return`;
		return this.http.post<any[]>(returnUrl, null);
	}

	// Add a method to fetch data after every action
	fetchDataAfterAction(action: () => Observable<any[]>): Observable<any[]> {
		return action().pipe(
			switchMap(() => this.getData())
		);
	}
}
