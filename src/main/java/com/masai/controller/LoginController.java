package com.masai.controller;

import java.net.URI;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.Customer;
import com.masai.dto.CustomerDTO;
import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.security.JwtUtil;
import com.masai.repository.CustomerRepository;
import com.masai.repository.SellerRepository;
import com.masai.service.interfaces.CustomerService;
import com.masai.service.interfaces.SellerService;
import com.masai.dto.AuthResponse;

@RestController
public class LoginController {

    private final CustomerService customerService;
    private final SellerService sellerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(CustomerService customerService, SellerService sellerService) {
        this.customerService = customerService;
        this.sellerService = sellerService;
    }

    @PostMapping(value = "/register/customer", consumes = "application/json")
    public ResponseEntity<Customer> registerAccountHandler(@Valid @RequestBody Customer customer) {
        Customer created = customerService.addCustomer(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping(value = "/register/seller", consumes = "application/json")
    public ResponseEntity<Seller> registerSellerAccountHandler(@Valid @RequestBody Seller seller) {
        Seller created = sellerService.addSeller(seller);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getSellerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping(value = "/login/customer", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginCustomerHandler(@Valid @RequestBody CustomerDTO customerDto){
        String principal = customerDto.getMobileId();
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal, customerDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), "ROLE_CUSTOMER");
        AuthResponse resp = new AuthResponse(jwt, jwtUtil.getJwtExpirationInMs(), "CUSTOMER");
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "/login/seller", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginSellerHandler(@Valid @RequestBody SellerDTO seller){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(seller.getMobile(), seller.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), "ROLE_SELLER");
        AuthResponse resp = new AuthResponse(jwt, jwtUtil.getJwtExpirationInMs(), "SELLER");
        return ResponseEntity.ok(resp);
    }
}
