import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';


@NgModule({
  imports: [BrowserModule, HttpClientModule],
  declarations: [AppComponent, RegisterComponent, LoginComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }