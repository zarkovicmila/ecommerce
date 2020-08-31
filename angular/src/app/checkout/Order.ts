import { OrderItem } from './OrderItem';

export class Order {
    constructor(public uuid:string, public customerEmail:string, public customerId:string, public orderTotal: number, public status: string, public createdDate: Date, public lastModifiedDate: Date, public orderItems: OrderItem[]){
        
    }
}