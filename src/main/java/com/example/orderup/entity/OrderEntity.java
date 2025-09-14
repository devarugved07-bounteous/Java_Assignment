package com.example.orderup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private Instant createdAt = Instant.now();
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference   // 👈 prevents infinite recursion
    private Product product;

    public OrderEntity(){}
    public OrderEntity(int qty, String status){
        this.quantity=qty;
        this.status=status;
    }

    public Long getId(){ return id; }
    public int getQuantity(){ return quantity; }
    public void setQuantity(int q){ this.quantity=q; }
    public Instant getCreatedAt(){ return createdAt; }
    public String getStatus(){ return status; }
    public void setStatus(String s){ this.status=s; }
    public Product getProduct(){ return product; }
    public void setProduct(Product p){ this.product = p; }
}
