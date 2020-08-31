package fon.orderservice.web.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OrderPagedList extends PageImpl<OrderDto> {

    public OrderPagedList(List<OrderDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public OrderPagedList(List<OrderDto> content) {
        super(content);
    }
}
