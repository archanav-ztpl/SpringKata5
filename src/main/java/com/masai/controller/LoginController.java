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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.Customer;
import com.masai.dto.CustomerDTO;
import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.dto.CreateSellerDTO;
import com.masai.security.JwtUtil;
import com.masai.repository.CustomerRepository;
import com.masai.repository.SellerRepository;
import com.masai.service.interfaces.CustomerService;
import com.masai.service.interfaces.SellerService;
import com.masai.dto.AuthResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Login Controller", description = "APIs for user authentication and registration")
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

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

    @Operation(summary = "Register a new customer", description = "Creates a new customer account")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Customer object containing customer details", required = true, content = @Content(schema = @Schema(implementation = Customer.class)))
    @PostMapping(value = "/register/customer", consumes = "application/json")
    public ResponseEntity<Customer> registerAccountHandler(@Valid @RequestBody Customer customer) {
        Customer created = customerService.addCustomer(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Register a new seller", description = "Creates a new seller account")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller creation data", required = true, content = @Content(schema = @Schema(implementation = CreateSellerDTO.class)))
    @PostMapping(value = "/register/seller", consumes = "application/json")
    public ResponseEntity<Seller> registerSellerAccountHandler(@Valid @RequestBody CreateSellerDTO createSellerDTO) {
        Seller created = sellerService.createSeller(createSellerDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getSellerId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Customer login", description = "Authenticates a customer and returns a JWT token")
    @PostMapping(value = "/login/customer", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginCustomerHandler(@Valid @RequestBody CustomerDTO customerDto){
        logger.info("Attempting login for customer: {}", customerDto.getMobileId());
        String principal = customerDto.getMobileId();
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principal, customerDto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Get customer by mobile number to extract ID
        Optional<Customer> customer = customerRepository.findByMobileNo(customerDto.getMobileId());
        if (customer.isPresent()) {
            String jwt = jwtUtil.generateToken(customer.get().getCustomerId());
            AuthResponse resp = new AuthResponse(jwt, jwtUtil.getJwtExpirationInMs(), "CUSTOMER");
            logger.info("Login successful for customer: {}", customerDto.getMobileId());
            return ResponseEntity.ok(resp);
        } else {
            throw new RuntimeException("Customer not found after successful authentication");
        }
    }

    @Operation(summary = "Seller login", description = "Authenticates a seller and returns a JWT token")
    @PostMapping(value = "/login/seller", consumes = "application/json")
    public ResponseEntity<AuthResponse> loginSellerHandler(@Valid @RequestBody SellerDTO seller){
        logger.info("Attempting login for seller: {}", seller.getMobile());
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(seller.getMobile(), seller.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // Get seller by mobile number to extract ID
        Optional<Seller> sellerEntity = sellerRepository.findByMobile(seller.getMobile());
        if (sellerEntity.isPresent()) {
            String jwt = jwtUtil.generateToken(sellerEntity.get().getSellerId());
            AuthResponse resp = new AuthResponse(jwt, jwtUtil.getJwtExpirationInMs(), "SELLER");
            logger.info("Login successful for seller: {}", seller.getMobile());
            return ResponseEntity.ok(resp);
        } else {
            throw new RuntimeException("Seller not found after successful authentication");
        }
    }
}
