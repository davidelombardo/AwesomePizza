package com.example.AwesomePizza.service;

import com.example.AwesomePizza.dto.OrderResponse;
import com.example.AwesomePizza.dto.OrderTO;
import com.example.AwesomePizza.dto.PizzaTypeTO;
import com.example.AwesomePizza.exception.NoSuchOrderException;
import com.example.AwesomePizza.service.impl.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private NoSuchOrderException noSuchOrderException;
    private Map<Integer, OrderResponse> orders = new HashMap<>();


    @Before
    public void setUp() {
        orderService = new OrderServiceImpl();
    }

    @Test
    public void createOrderSuccess() {
        List<PizzaTypeTO> pizzaTypeTOList = buildListPizzaType();
        OrderTO orderTO = new OrderTO();
        orderTO.setPizzaTypeTOList(pizzaTypeTOList);

        // Calling the service method
        List<OrderResponse> result = orderService.createOrder(Arrays.asList(orderTO));

        // Verifying the result
        assertEquals(1, result.size());
        assertEquals("Order in preparation", result.get(0).getStatus());
    }

    @Test
    public void checkOrderFound() {
        int orderNumber = 1;
        OrderResponse orderResponse = buildOrderResponse();
        orders.put(orderNumber, orderResponse);

        // Creating a custom OrderServiceImpl instance for testing
        OrderServiceImpl orderServiceWithCustomOrders = new OrderServiceImpl(orders, new AtomicInteger(2));

        // Calling the service method
        OrderResponse result = orderServiceWithCustomOrders.checkOrder(orderNumber);

        // Verifying the result
        assertEquals(orderResponse, result);
    }

    @Test(expected = NoSuchOrderException.class)
    public void testCheckOrder_OrderNotFound() {
        int orderNumber = 1;
        orderService.checkOrder(orderNumber);
    }

    @Test
    public void testCheckOrder_OrderNotReady() {
        // Mocking input data
        int orderNumber = 2;
        OrderResponse orderResponse = buildOrderResponse();
        orderResponse.setNumberOrder(2);
        orders.put(1, orderResponse);
        orders.put(2, orderResponse);

        OrderServiceImpl orderServiceWithCustomOrders = new OrderServiceImpl(orders, new AtomicInteger(2));

        // Calling the service method
        OrderResponse result = orderServiceWithCustomOrders.checkOrder(orderNumber);

        assertEquals("The order " + orderNumber + " is not ready", result.getStatus());
    }

    @Test
    public void testCheckOrder_OrderReady() {
        int orderNumber = 1;
        OrderResponse orderResponse = buildOrderResponse();
        orders.put(orderNumber, orderResponse);
        OrderServiceImpl orderServiceWithCustomOrders = new OrderServiceImpl(orders, new AtomicInteger(1));

        OrderResponse result = orderServiceWithCustomOrders.checkOrder(orderNumber);
        assertEquals("The order " + orderNumber + " is ready", result.getStatus());
    }

    private OrderResponse buildOrderResponse() {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setNumberOrder(1);
        orderResponse.setStatus("Order in preparation");
        return orderResponse;
    }

    private List<PizzaTypeTO> buildListPizzaType() {
        PizzaTypeTO pizzaTypeTO = new PizzaTypeTO();
        pizzaTypeTO.setPizzaType("Margherita");
        pizzaTypeTO.setQuantity(2);
        return Arrays.asList(pizzaTypeTO);
    }
}
