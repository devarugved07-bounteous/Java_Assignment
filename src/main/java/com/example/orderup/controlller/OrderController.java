package com.example.orderup.controlller;

import com.example.orderup.dto.OrderRequest;
import com.example.orderup.dto.OrderResponseDto;
import com.example.orderup.entity.OrderEntity;
import com.example.orderup.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequest req) {
        OrderEntity order = orderService.placeOrder(req.productId(), req.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(order));
    }


    @PostMapping("/batch")
    public ResponseEntity<List<OrderResponseDto>> placeOrders(@RequestBody List<OrderRequest> requests) {
        List<OrderResponseDto> responses = requests.stream()
                .map(req -> mapToDto(orderService.placeOrder(req.productId(), req.quantity())))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    private OrderResponseDto mapToDto(OrderEntity order) {
        return new OrderResponseDto(
                order.getId(),
                order.getQuantity(),
                order.getCreatedAt(),
                order.getStatus(),
                new OrderResponseDto.ProductDto(
                        order.getProduct().getId(),
                        order.getProduct().getName(),
                        order.getProduct().getStock()
                )
        );
    }
}
