import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Customer } from './Customer';
import { AppService } from '../app.service';
import { Order } from './Order';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  customer = new Customer('','','','');
  city: string = "";
  street: string = "";
  state: string = "";
  zip: string = "";
  order = new Order('','','',0,'',null,null,[]);

  constructor(private location: Location, private service: AppService, private router: Router) { }

  ngOnInit() {
  }

  goBack() {
    this.location.back();
  }

  public checkout(){
    console.log('Checkout');
    this.getCart();
    this.saveCustomer();
   
  }


  private placeOrder(){
    this.service.placeOrder(this.order).subscribe(
      res =>{
        this.deleteCart();
        this.router.navigate(['catalog']);
        console.log(res);
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

  private getCart(){
    this.service.getCart().subscribe(
      res => {
        this.order.orderItems = res;
        this.order.customerEmail = this.customer.email;
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

  private saveCustomer(){
    let address = this.street + " " + this.city + " " + this.state + " " + this.zip;
    this.customer.address = address;

    this.service.saveCustomer(this.customer).subscribe(
      res =>{
        this.placeOrder();
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

  private deleteCart(){
    this.service.deleteCart().subscribe(
      res => {
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

}

