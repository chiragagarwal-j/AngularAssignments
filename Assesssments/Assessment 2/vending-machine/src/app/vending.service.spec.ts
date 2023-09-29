import { TestBed } from '@angular/core/testing';

import { VendingService } from './vending.service';

describe('VendingService', () => {
  let service: VendingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VendingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
