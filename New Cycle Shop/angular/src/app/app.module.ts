import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { CyclesComponent } from './cycles/cycles.component';
import { FormsModule } from '@angular/forms';
import { RequestInterceptor } from './request.interceptor';


@NgModule({
  imports: [BrowserModule, HttpClientModule, AppRoutingModule, FormsModule],
  declarations: [AppComponent, RegisterComponent, LoginComponent, CyclesComponent],
  providers: [{provide:HTTP_INTERCEPTORS,useClass:RequestInterceptor,multi:true}],
  bootstrap: [AppComponent]
})
export class AppModule { }