import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { Item } from './CartItem';
import { cpuUsage } from 'process';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartItems: Item[] = [];
  total: number = 0;

  constructor(private service: AppService) { }

  ngOnInit() {
    this.getCart();
  }

  public getCart(){
    this.service.getCart().subscribe(
      res => {
        this.cartItems = res;
        this.countTotal();
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

  public removeItemFromCart(index: number){
    this.service.deleteItemFromCart(this.cartItems[index].productId).subscribe(
      res => {
        this.cartItems.splice(index,1);
        this.countTotal();
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }

  public updatedItemQuantity(updatedItemQuantity: Item){
    updatedItemQuantity.subtotal = updatedItemQuantity.quantity*updatedItemQuantity.price;
    console.log(updatedItemQuantity);
    this.service.updateItemQuantity(updatedItemQuantity).subscribe(
      res=> {
        this.countTotal();
      },
      err => {
        console.log("Error "+ err.message);
      }
    ); 
  }

  private countTotal(){
    this.total = 0;
    this.cartItems.forEach(item => {
      this.total = this.total + item.subtotal;
    });
  }

 
}

