package com.example.AwesomePizza.controller;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;
import com.example.AwesomePizza.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/orders")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;
    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrder(@RequestBody List<OrderTO> orderTOList){
        List<OrderResponse> orderResponses = new ArrayList<>();
        if(Objects.nonNull(orderTOList) && !orderTOList.isEmpty()){
            orderResponses = orderService.createOrder(orderTOList);
        }else{
            log.error("cannot create order because the order list is null");
            throw new RuntimeException("Cannot create order because the order list is null");
        }
        if(Objects.nonNull(orderResponses) && !orderResponses.isEmpty()){
            log.info("order created");
            return new ResponseEntity<>(orderResponses, HttpStatus.CREATED);
        }else{
            log.error("cannot create order because order response is null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<OrderResponse> checkStatusOrder(@RequestParam int numberOrder){
        OrderResponse orderResponse = orderService.checkOrder(numberOrder);
        if(Objects.nonNull(orderResponse) ){
            log.info("check order");
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        }else{
            log.error("cannot check order because order response is null");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
