import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CyclesComponent } from './cycles/cycles.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { RequestInterceptor } from './request.interceptor';
import { RestockComponent } from './restock/restock.component';
import { AuthClickDirective } from './auth-click.directive';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CartComponent } from './cart/cart.component';
import { BaseLayoutComponent } from './base-layout/base-layout.component';
import { SiteLayoutComponent } from './site-layout/site-layout.component';
@NgModule({
  declarations: [
    AppComponent,
    CyclesComponent,
    NavbarComponent,
    LoginFormComponent,
    RestockComponent,
    AuthClickDirective,
    CartComponent,
    BaseLayoutComponent,
    SiteLayoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    RouterModule,
    FormsModule,
    HttpClientModule,
    FontAwesomeModule
  ],
  providers: [{provide:HTTP_INTERCEPTORS,useClass:RequestInterceptor,multi:true}],
  bootstrap: [AppComponent]
})
export class AppModule { }
