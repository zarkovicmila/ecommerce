import { Component, OnInit, ViewChildren, QueryList } from '@angular/core';
import { ProductComponent } from '../product/product.component';
import { Product } from '../product/Product';
import { AppService } from '../app.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {

  products: Product[] = [];
  type = "";

  @ViewChildren('child') children : QueryList<ProductComponent>;
  
  constructor(private service: AppService, private route: ActivatedRoute) { }
 
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.type = params['type'];
      this.getAllProducts();
    });
   }

  public getAllProducts(){
    this.service.getProducts(this.type).subscribe(
      res => {
        this.products = res.body;
      }, 
      err => {
        console.log('Error');
      }
    );
  }

  public getAllProductsPagination(){
    this.service.getProductsPagination(0).subscribe(
      res => {
        this.products = res.content;
      }, 
      err => {
        console.log('Error');
      }
    );
  }

}

