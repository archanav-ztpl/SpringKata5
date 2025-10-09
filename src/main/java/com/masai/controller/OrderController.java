package com.masai.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.masai.model.Customer;
import com.masai.model.Order;
import com.masai.dto.OrderDTO;
import com.masai.service.interfaces.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Order Controller", description = "APIs for managing orders")
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Place a new order", description = "Creates a new order for the logged-in user")
    @PostMapping
    public ResponseEntity<Order> addTheNewOrder(@Valid @RequestBody OrderDTO orderDto, @RequestHeader("token") String token) {
        logger.info("Placing a new order with token: {}", token);
        Order saveOrder = orderService.saveOrder(orderDto, token);
        logger.info("Order placed successfully");
        return new ResponseEntity<>(saveOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all orders", description = "Fetches a list of all orders")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){

        List<Order> listOfAllOrders = orderService.getAllOrders();
        return ResponseEntity.ok(listOfAllOrders);

    }

    @Operation(summary = "Get order by ID", description = "Fetches details of an order by its ID")
    @Parameter(name = "orderId", description = "ID of the order", required = true)
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrdersByOrderId(@PathVariable("orderId") Integer orderId) {

        Order order = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(order);

    }

    @Operation(summary = "Cancel an order", description = "Cancels an order by its ID")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> cancelTheOrderByOrderId(@PathVariable("orderId") Integer orderId,@RequestHeader("token") String token){

        Order cancelled = orderService.cancelOrderByOrderId(orderId,token);
        return ResponseEntity.ok(cancelled);
    }

    @Operation(summary = "Update an order", description = "Updates an order by its ID")
    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderByOrder(@Valid @RequestBody OrderDTO orderDto, @PathVariable("orderId") Integer orderId,@RequestHeader("token") String token){

        Order updatedOrder= orderService.updateOrderByOrder(orderDto,orderId,token);

        return ResponseEntity.ok(updatedOrder);

    }

    @GetMapping("/by/date")
    public ResponseEntity<List<Order>> getOrdersByDate(@RequestParam("date") String date){

        DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate ld=LocalDate.parse(date,dtf);
        List<Order> orders = orderService.getAllOrdersByDate(ld);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/customer/{orderId}")
    public ResponseEntity<Customer> getCustomerDetailsByOrderId(@PathVariable("orderId") Integer orderId) {
        Customer customer = orderService.getCustomerByOrderid(orderId);
        return ResponseEntity.ok(customer);
    }

}
