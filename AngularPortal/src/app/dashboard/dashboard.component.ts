import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor() {
  }

  imagesUrl = [];
  ngOnInit() {
    this.imagesUrl = [
      'https://mdbootstrap.com/img/Photos/Horizontal/Nature/4-col/img%20(38).jpg',
      'https://mdbootstrap.com/img/Photos/Horizontal/Nature/4-col/img%20(19).jpg',
      'https://mdbootstrap.com/img/Photos/Horizontal/Nature/4-col/img%20(42).jpg',
      'https://mdbootstrap.com/img/Photos/Horizontal/Nature/4-col/img%20(8).jpg',
    ];
  }

}
