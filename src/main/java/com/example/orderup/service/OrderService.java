package com.example.orderup.service;

import com.example.orderup.entity.OrderEntity;
import com.example.orderup.entity.Product;
import com.example.orderup.exception.InsufficientStockException;
import com.example.orderup.exception.NotFoundException;
import com.example.orderup.repository.OrderRepository;
import com.example.orderup.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public OrderService(ProductRepository productRepo, OrderRepository orderRepo){
        this.productRepo = productRepo; this.orderRepo = orderRepo;
    }
    @Transactional
    public OrderEntity placeOrder(Long productId, int qty){
        if(qty<=0) throw new IllegalArgumentException("Quantity must be > 0");
        ReentrantLock lock = locks.computeIfAbsent(productId, id->new ReentrantLock());
        lock.lock();
        try { return doPlaceOrder(productId, qty); }
        finally { lock.unlock(); }
    }

    @Transactional
    protected OrderEntity doPlaceOrder(Long productId, int qty){
        Product product = productRepo.findProductById(productId);
        if(product==null) throw new NotFoundException("Product not found " + productId);
        if(product.getStock()<qty) throw new InsufficientStockException("Not enough stock");

        product.setStock(product.getStock()-qty);
        OrderEntity order = new OrderEntity(qty,"RESERVED");
        product.addOrder(order);
        productRepo.save(product);
        return orderRepo.save(order);
    }
}
