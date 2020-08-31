import { Component, OnInit, Input } from '@angular/core';
import { Product } from './Product';
import { Item } from '../cart/CartItem';
import { AppService } from '../app.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input('product') product: Product;
  item: Item;
  
  constructor(private service: AppService) { }

  ngOnInit() {
  }

  public addProductToCart(){
    this.item = new Item(this.product.uuid, this.product.productName, this.product.productCode, this.product.price, 1, this.product.price, this.product.url);

    this.service.addProductToCart(this.item).subscribe(
      res => {
      },
      err => {
        console.log("Error "+ err.message);
      }
    );
  }



}


