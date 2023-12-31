import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CyclesComponent } from './cycles.component';

describe('CyclesComponent', () => {
  let component: CyclesComponent;
  let fixture: ComponentFixture<CyclesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CyclesComponent]
    });
    fixture = TestBed.createComponent(CyclesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
