package fon.orderservice.service.customer;

import fon.orderservice.web.model.CustomerDto;


import java.util.UUID;

public interface CustomerService {


    CustomerDto saveCustomer(CustomerDto customerDto);

    CustomerDto findCustomerById(UUID uuid);

    CustomerDto findCustomerByEmail(String email);
}
