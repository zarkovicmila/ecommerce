package fon.orderservice.repository;

import fon.orderservice.domain.Order;
import fon.orderservice.domain.OrderItem;
import fon.orderservice.domain.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

     void deleteByOrder(Order order);
}
