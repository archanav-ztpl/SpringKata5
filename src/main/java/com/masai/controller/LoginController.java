package com.masai.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.Customer;
import com.masai.dto.CustomerDTO;
import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;
import com.masai.model.UserSession;
import com.masai.service.interfaces.CustomerService;
import com.masai.service.interfaces.LoginLogoutService;
import com.masai.service.interfaces.SellerService;

@RestController
public class LoginController {

    private final CustomerService customerService;
    private final LoginLogoutService loginService;
    private final SellerService sellerService;

    @Autowired
    public LoginController(CustomerService customerService, LoginLogoutService loginService, SellerService sellerService) {
        this.customerService = customerService;
        this.loginService = loginService;
        this.sellerService = sellerService;
    }


    // Handler to register a new customer

    @PostMapping(value = "/register/customer", consumes = "application/json")
    public ResponseEntity<Customer> registerAccountHandler(@Valid @RequestBody Customer customer) {
        Customer created = customerService.addCustomer(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // Handler to login a user

    @PostMapping(value = "/login/customer", consumes = "application/json")
    public ResponseEntity<UserSession> loginCustomerHandler(@Valid @RequestBody CustomerDTO customerDto){
        return ResponseEntity.ok(loginService.loginCustomer(customerDto));
    }


    // Handler to logout a user

    @PostMapping(value = "/logout/customer", consumes = "application/json")
    public ResponseEntity<SessionDTO> logoutCustomerHandler(@RequestBody SessionDTO sessionToken){
        return ResponseEntity.ok(loginService.logoutCustomer(sessionToken));
    }



    /*********** SELLER REGISTER LOGIN LOGOUT HANDLER ************/

    @PostMapping(value = "/register/seller", consumes = "application/json")
    public ResponseEntity<Seller> registerSellerAccountHandler(@Valid @RequestBody Seller seller) {
        Seller created = sellerService.addSeller(seller);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getSellerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }


    // Handler to login a user

    @PostMapping(value = "/login/seller", consumes = "application/json")
    public ResponseEntity<UserSession> loginSellerHandler(@Valid @RequestBody SellerDTO seller){
        return ResponseEntity.ok(loginService.loginSeller(seller));
    }


    // Handler to logout a user

    @PostMapping(value = "/logout/seller", consumes = "application/json")
    public ResponseEntity<SessionDTO> logoutSellerHandler(@RequestBody SessionDTO sessionToken){
        return ResponseEntity.ok(loginService.logoutSeller(sessionToken));
    }

}
