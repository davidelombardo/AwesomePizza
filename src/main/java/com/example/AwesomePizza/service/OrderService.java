package com.example.AwesomePizza.service;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;

import java.util.List;

public interface OrderService {

    List<OrderResponse> createOrder(List<OrderTO> orderTOList);

    OrderResponse checkOrder(int orderNumber);
}
