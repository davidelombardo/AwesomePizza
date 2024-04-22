package com.example.AwesomePizza.controller;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;
import com.example.AwesomePizza.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;
    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrder(@RequestBody List<OrderTO> orderTOList){
        List<OrderResponse> orderResponses = new ArrayList<>();
        if(Objects.nonNull(orderTOList) && !orderTOList.isEmpty()){
            orderResponses = orderService.createOrder(orderTOList);
        }else{
            throw new RuntimeException("Cannot create Order");
        }
        if(Objects.nonNull(orderResponses) && !orderResponses.isEmpty()){
            return new ResponseEntity<>(orderResponses, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<OrderResponse> checkStatusOrder(@RequestParam int numberOrder){
        OrderResponse orderResponse = orderService.checkOrder(numberOrder);
        if(Objects.nonNull(orderResponse) ){
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
