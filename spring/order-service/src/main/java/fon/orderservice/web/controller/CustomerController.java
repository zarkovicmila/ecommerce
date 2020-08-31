package fon.orderservice.web.controller;

import fon.orderservice.service.customer.CustomerService;
import fon.orderservice.web.model.CustomerDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("customer")
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.saveCustomer(customerDto), HttpStatus.CREATED);
    }

    @GetMapping("customer/{customerId}")
    public ResponseEntity findCustomer(@PathVariable("customerId")UUID customerId){
        return new ResponseEntity(customerService.findCustomerById(customerId), HttpStatus.OK);
    }
}
