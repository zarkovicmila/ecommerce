import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Product } from './product/Product';
import { environment } from './../environments/environment';
import { Item } from './cart/CartItem';
import { Order } from './checkout/Order';
import { Customer } from './checkout/Customer';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  constructor(private http:HttpClient) { }

  getProducts(value?: string): Observable<HttpResponse<Product[]>> {
    let param = "";
    if(value !== undefined){
      param = "?type=" + value;
    }
    return this.http.get<Product[]>(environment.apiUrl + 'product-service/api/v1/product' + param, {observe: 'response'});
  }

  getProductsPagination(pageNumber: number): Observable<any> {
    return this.http.get<Product[]>(environment.apiUrl + 'product-service/api/v1/products?pageNumber=' + pageNumber);
  }

  addProductToCart(item: Item): Observable<any> {
    return this.http.post(environment.apiUrl + 'cart-service/api/v1/cart/' + item.productId, item);
  }

  getCart(): Observable<any> {
    return this.http.get(environment.apiUrl + 'cart-service/api/v1/cart');
  }

  deleteItemFromCart(productId: string): Observable<any> {
    return this.http.delete(environment.apiUrl + 'cart-service/api/v1/cart/'+ productId);
  }

  deleteCart(): Observable<any> {
    return this.http.delete(environment.apiUrl + 'cart-service/api/v1/cart');
  }

  updateItemQuantity(item: Item): Observable<any> {
    return this.http.put(environment.apiUrl + 'cart-service/api/v1/cart/'+ item.productId + 
    '?quantity='+item.quantity, item.quantity);
  }

  placeOrder(order: Order): Observable<any> {
    return this.http.post(environment.apiUrl + 'order-service/api/v1/order', order);
  }

  saveCustomer(customer: Customer): Observable<any> {
    return this.http.post(environment.apiUrl + 'order-service/api/v1/customer', customer);
  }
}
