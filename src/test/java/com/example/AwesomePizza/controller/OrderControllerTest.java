package com.example.AwesomePizza.controller;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;
import com.example.AwesomePizza.dto.PizzaTypeTO;
import com.example.AwesomePizza.service.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void createOrderSuccess() {
        List<PizzaTypeTO> pizzaTypeTOList = buildListPizzaType();
        OrderTO orderTO = new OrderTO();
        orderTO.setPizzaTypeTOList(pizzaTypeTOList);

        List<OrderResponse> orderResponses = buildOrderResponse();
        when(orderService.createOrder(Arrays.asList(orderTO))).thenReturn(orderResponses);

        ResponseEntity<List<OrderResponse>> responseEntity = orderController.createOrder(Arrays.asList(orderTO));

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(orderResponses, responseEntity.getBody());

    }



    @Test
    public void createOrderEmptyInput() {
        List<OrderTO> orderTOList = new ArrayList<>();

        try {
            orderController.createOrder(orderTOList);
        } catch (RuntimeException e) {
            assertEquals("Cannot create Order", e.getMessage());
        }
    }

    @Test
    public void createOrderNoResponse() {
        List<PizzaTypeTO> pizzaTypeTOList = buildListPizzaType();
        OrderTO orderTO = new OrderTO();

        when(orderService.createOrder(Arrays.asList(orderTO))).thenReturn(null);

        ResponseEntity<List<OrderResponse>> responseEntity = orderController.createOrder(Arrays.asList(orderTO));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    public void checkStatusOrderSuccess() {
        int numberOrder = 123;
        OrderResponse orderResponse = buildOrderResponse().stream().findFirst().get();

        when(orderService.checkOrder(numberOrder)).thenReturn(orderResponse);

        ResponseEntity<OrderResponse> responseEntity = orderController.checkStatusOrder(numberOrder);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orderResponse, responseEntity.getBody());

    }

    @Test
    public void testCheckStatusOrderNotFound() {
        int numberOrder = 123;
        when(orderService.checkOrder(numberOrder)).thenReturn(null);

        ResponseEntity<OrderResponse> responseEntity = orderController.checkStatusOrder(numberOrder);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    private List<OrderResponse> buildOrderResponse() {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setNumberOrder(1);
        orderResponse.setStatus("The order " + orderResponse.getNumberOrder() + " is ready");
        return Arrays.asList(orderResponse);
    }

    private List<PizzaTypeTO> buildListPizzaType() {
        PizzaTypeTO pizzaTypeTO = new PizzaTypeTO();
        pizzaTypeTO.setPizzaType("Margherita");
        pizzaTypeTO.setQuantity(2);
        return Arrays.asList(pizzaTypeTO);
    }
}
