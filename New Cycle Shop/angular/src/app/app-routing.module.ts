import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CyclesComponent } from './cycles/cycles.component';


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'cycles', component: CyclesComponent},
  {path: '', redirectTo: '/cycles', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
