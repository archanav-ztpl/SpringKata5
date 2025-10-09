package com.masai.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masai.model.Address;
import com.masai.model.CreditCard;
import com.masai.model.Customer;
import com.masai.dto.CustomerDTO;
import com.masai.dto.CustomerUpdateDTO;
import com.masai.model.Order;
import com.masai.dto.SessionDTO;
import com.masai.service.interfaces.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Customer Controller", description = "APIs for managing customers")
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Handler to get a list of all customers

    @Operation(summary = "Get all customers", description = "Fetches a list of all customers")
    @Parameter(name = "token", description = "Authentication token", required = true)
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomersHandler(@RequestHeader("token") String token){
        logger.info("Fetching all customers with token: {}", token);
        List<Customer> customers = customerService.getAllCustomers(token);
        logger.info("Fetched {} customers successfully", customers.size());
        return ResponseEntity.ok(customers);
    }


    // Handler to Get a customer details of currently logged in user - sends data as per token

    @Operation(summary = "Get logged-in customer details", description = "Fetches details of the currently logged-in customer")
    @GetMapping("/current")
    public ResponseEntity<Customer> getLoggedInCustomerDetailsHandler(@RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.getLoggedInCustomerDetails(token));
    }


    // Handler to Update a customer

    @Operation(summary = "Update customer details", description = "Updates the details of the logged-in customer")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CustomerUpdateDTO object containing updated details", required = true, content = @Content(schema = @Schema(implementation = CustomerUpdateDTO.class)))
    @PutMapping
    public ResponseEntity<Customer> updateCustomerHandler(@Valid @RequestBody CustomerUpdateDTO customerUpdate, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomer(customerUpdate, token));
    }


    // Handler to update a customer email-id or mobile no
    @Operation(summary = "Update customer credentials", description = "Updates the email or mobile number of the logged-in customer")
    @PutMapping("/credentials")
    public ResponseEntity<Customer> updateCustomerMobileEmailHandler(@Valid @RequestBody CustomerUpdateDTO customerUpdate, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomerMobileNoOrEmailId(customerUpdate, token));
    }


    // Handler to update customer password
    @Operation(summary = "Update customer password", description = "Updates the password of the logged-in customer")
    @PutMapping("/password")
    public ResponseEntity<SessionDTO> updateCustomerPasswordHandler(@Valid @RequestBody CustomerDTO customerDto, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateCustomerPassword(customerDto, token));
    }


    // Handler to Add or update new customer Address
    @PutMapping("/address")
    public ResponseEntity<Customer> updateAddressHandler(@Valid @RequestBody Address address, @RequestParam("type") String type, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.updateAddress(address, type, token));
    }


    // Handler to update Credit card details
    @PutMapping("/card")
    public ResponseEntity<Customer> updateCreditCardHandler(@RequestHeader("token") String token, @Valid @RequestBody CreditCard newCard){
        return ResponseEntity.ok(customerService.updateCreditCardDetails(token, newCard));
    }


    // Handler to Remove a user address
    @Operation(summary = "Remove customer address", description = "Removes an address of the logged-in customer")
    @DeleteMapping("/address")
    public ResponseEntity<Customer> deleteAddressHandler(@RequestParam("type") String type, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.deleteAddress(type, token));
    }

    // Handler to delete customer
    @DeleteMapping
    public ResponseEntity<SessionDTO> deleteCustomerHandler(@Valid @RequestBody CustomerDTO customerDto, @RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.deleteCustomer(customerDto, token));
    }



    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getCustomerOrdersHandler(@RequestHeader("token") String token){
        return ResponseEntity.ok(customerService.getCustomerOrders(token));
    }
}
