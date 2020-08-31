export class OrderItem {
    constructor(public serialNumber:number, public orderId:string, public productId:string, public productCode:string, public price:number, public quantity:number, public subtotal:number, public quantityAllocated:number){
        
    }
}