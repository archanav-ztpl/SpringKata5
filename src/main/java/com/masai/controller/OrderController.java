package com.masai.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.masai.model.Customer;
import com.masai.model.Order;
import com.masai.dto.OrderDTO;
import com.masai.service.interfaces.OrderService;

@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/place")
    public ResponseEntity<Order> addTheNewOrder(@Valid @RequestBody OrderDTO orderDto,@RequestHeader("token") String token){

        Order saveOrder = orderService.saveOrder(orderDto,token);
        return new ResponseEntity<Order>(saveOrder,HttpStatus.CREATED);

    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders(){

        List<Order> listOfAllOrders = orderService.getAllOrders();
        return ResponseEntity.ok(listOfAllOrders);

    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrdersByOrderId(@PathVariable("orderId") Integer orderId) {

        Order order = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(order);

    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Order> cancelTheOrderByOrderId(@PathVariable("orderId") Integer orderId,@RequestHeader("token") String token){

        Order cancelled = orderService.cancelOrderByOrderId(orderId,token);
        return ResponseEntity.ok(cancelled);
    }

    @PutMapping("/orders/{orderId}")
    public ResponseEntity<Order> updateOrderByOrder(@Valid @RequestBody OrderDTO orderDto, @PathVariable("orderId") Integer orderId,@RequestHeader("token") String token){

        Order updatedOrder= orderService.updateOrderByOrder(orderDto,orderId,token);

        return ResponseEntity.ok(updatedOrder);

    }

    @GetMapping("/orders/by/date")
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
