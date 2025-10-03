package com.masai.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masai.model.Address;
import com.masai.model.CreditCard;
import com.masai.model.Customer;
import com.masai.dto.CustomerDTO;
import com.masai.dto.CustomerUpdateDTO;
import com.masai.model.Order;
import com.masai.dto.SessionDTO;
import com.masai.service.interfaces.CustomerService;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Handler to get a list of all customers

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomersHandler(@RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.getAllCustomers(token));
    }


    // Handler to Get a customer details of currently logged in user - sends data as per token

    @GetMapping("/customer/current")
    public ResponseEntity<Customer> getLoggedInCustomerDetailsHandler(@RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.getLoggedInCustomerDetails(token));
    }


    // Handler to Update a customer

    @PutMapping("/customer")
    public ResponseEntity<Customer> updateCustomerHandler(@Valid @RequestBody CustomerUpdateDTO customerUpdate, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomer(customerUpdate, token));
    }


    // Handler to update a customer email-id or mobile no
    @PutMapping("/customer/update/credentials")
    public ResponseEntity<Customer> updateCustomerMobileEmailHandler(@Valid @RequestBody CustomerUpdateDTO customerUpdate, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomerMobileNoOrEmailId(customerUpdate, token));
    }


    // Handler to update customer password
    @PutMapping("/customer/update/password")
    public ResponseEntity<SessionDTO> updateCustomerPasswordHandler(@Valid @RequestBody CustomerDTO customerDto, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomerPassword(customerDto, token));
    }


    // Handler to Add or update new customer Address
    @PutMapping("/customer/update/address")
    public ResponseEntity<Customer> updateAddressHandler(@Valid @RequestBody Address address, @RequestParam("type") String type, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateAddress(address, type, token));
    }


    // Handler to update Credit card details
    @PutMapping("/customer/update/card")
    public ResponseEntity<Customer> updateCreditCardHandler(@RequestHeader("token") String token, @Valid @RequestBody CreditCard newCard){
        return ResponseEntity.ok(customerService.updateCreditCardDetails(token, newCard));
    }


    // Handler to Remove a user address
    @DeleteMapping("/customer/delete/address")
    public ResponseEntity<Customer> deleteAddressHandler(@RequestParam("type") String type, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.deleteAddress(type, token));
    }

    // Handler to delete customer
    @DeleteMapping("/customer")
    public ResponseEntity<SessionDTO> deleteCustomerHandler(@Valid @RequestBody CustomerDTO customerDto, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.deleteCustomer(customerDto, token));
    }



    @GetMapping("/customer/orders")
    public ResponseEntity<List<Order>> getCustomerOrdersHandler(@RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.getCustomerOrders(token));
    }
}
