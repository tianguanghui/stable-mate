import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DisplayImageComponent } from './display-image/display-image.component';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { RegisterComponent } from './register/register.component';
import { AdminComponent } from './admin/admin.component';
import { DisplayClassComponent } from './display-class/display-class.component';
import { StarRatingModule } from 'angular-star-rating';
import { NewUserComponent } from './new-user/new-user.component';
import { routing } from './app.routing';
import { LoginComponent } from './login';
import { HomeComponent } from './home';
import { ReactiveFormsModule, FormsModule } from '../../node_modules/@angular/forms';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    DisplayImageComponent,
    PageNotFoundComponent,
    RegisterComponent,
    AdminComponent,
    DisplayClassComponent,
    NewUserComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent
  ],
  imports: [
    routing,
    BrowserModule,
    StarRatingModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
