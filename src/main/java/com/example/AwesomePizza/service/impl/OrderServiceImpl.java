package com.example.AwesomePizza.service.impl;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;
import com.example.AwesomePizza.exception.NoSuchOrderException;
import com.example.AwesomePizza.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private Map<Integer, OrderResponse> orders;
    private AtomicInteger nextNumberOrder;

    public OrderServiceImpl() {
        this.orders = new HashMap<>();
        this.nextNumberOrder = new AtomicInteger(1);
    }
    public OrderServiceImpl(Map<Integer, OrderResponse> orders, AtomicInteger nextNumberOrder) {
        this.orders = orders;
        this.nextNumberOrder = nextNumberOrder;
    }
    @Override
    public List<OrderResponse> createOrder(List<OrderTO> orderTOList) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        log.info("Begin orders creation");
        orderTOList.forEach(orderTO1 -> {
                OrderResponse orderResponse = new OrderResponse();
                int numberOrderIncremented = nextNumberOrder.getAndIncrement();
                orderResponse.setNumberOrder(numberOrderIncremented);
                orderResponse.setStatus("Order in preparation");
                this.orders.put(numberOrderIncremented, orderResponse);
                orderResponses.add(orderResponse);
            });
        return orderResponses;
    }

    @Override
    public OrderResponse checkOrder(int orderNumber) {
        OrderResponse orderResponse = orders.get(orderNumber);
        if (Objects.isNull(orderResponse)) {
            log.error("Order with number " + orderNumber + " not found.");
            throw new NoSuchOrderException("Order with number " + orderNumber + " not found.");
        }
        Integer firstOrderNumber = null;
        if (!orders.isEmpty()) {
            firstOrderNumber = orders.keySet().iterator().next();
        }
        if (Objects.nonNull(firstOrderNumber) && firstOrderNumber < orderNumber) {
            log.info("The order " + orderResponse.getNumberOrder() + " is not ready");
            orderResponse.setStatus("The order " + orderResponse.getNumberOrder() + " is not ready");
        } else {
            log.info("The order " + orderResponse.getNumberOrder() + " is ready");
            orderResponse.setStatus("The order " + orderResponse.getNumberOrder() + " is ready");
            orders.remove(orderNumber);
        }
        return orderResponse;
    }

}
