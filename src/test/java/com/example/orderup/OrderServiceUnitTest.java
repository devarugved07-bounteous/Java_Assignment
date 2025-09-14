package com.example.orderup;

import com.example.orderup.entity.*;
import com.example.orderup.exception.*;
import com.example.orderup.repository.*;
import com.example.orderup.service.OrderService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceUnitTest {

    @Test
    void success(){
        ProductRepository prod=mock(ProductRepository.class);
        OrderRepository ord=mock(OrderRepository.class);
        Product p=new Product("Vase",5);
        when(prod.findProductById(1L)).thenReturn(p);
        when(prod.save(any())).thenReturn(p);
        when(ord.save(any())).thenAnswer(i->i.getArgument(0));

        OrderService svc=new OrderService(prod,ord);
        OrderEntity o=svc.placeOrder(1L,2);
        assertEquals(2,o.getQuantity());
    }

    @Test
    void insufficient(){
        ProductRepository prod=mock(ProductRepository.class);
        OrderRepository ord=mock(OrderRepository.class);
        when(prod.findProductById(2L)).thenReturn(new Product("Cup",1));

        OrderService svc=new OrderService(prod,ord);
        assertThrows(InsufficientStockException.class,()->svc.placeOrder(2L,3));
    }
}
