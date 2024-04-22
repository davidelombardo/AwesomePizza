package com.example.AwesomePizza.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderTO {

    private int numberOrder;
    private List<PizzaTypeTO> pizzaTypeTOList;
    private String status;

}
