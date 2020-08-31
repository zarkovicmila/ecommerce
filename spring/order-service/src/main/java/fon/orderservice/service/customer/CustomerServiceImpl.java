package fon.orderservice.service.customer;

import fon.orderservice.domain.Customer;
import fon.orderservice.repository.CustomerRepository;
import fon.orderservice.web.mapper.CustomerMapper;
import fon.orderservice.web.model.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        Customer customer = customerRepository.findCustomerByEmail(customerDto.getEmail());

        if(customer != null){
            customer.setCustomerName(customerDto.getCustomerName());
            customer.setAddress(customerDto.getAddress());
            return customerMapper.customerToCustomerDto(customerRepository.save(customer));
        }
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customerDto)));
    }

    @Override
    public CustomerDto findCustomerById(UUID uuid) {
        return customerMapper.customerToCustomerDto(customerRepository.findById(uuid).orElseThrow(RuntimeException::new));
    }

    @Override
    public CustomerDto findCustomerByEmail(String email) {
        return customerMapper.customerToCustomerDto(customerRepository.findCustomerByEmail(email));
    }
}
