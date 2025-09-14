package com.example.orderup.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference   // 👈 prevents infinite recursion
    private List<OrderEntity> orders = new ArrayList<>();

    public Product() {}
    public Product(String name, int stock){
        this.name = name;
        this.stock = stock;
    }

    public Long getId(){ return id; }
    public String getName(){ return name; }
    public void setName(String n){ this.name = n; }
    public int getStock(){ return stock; }
    public void setStock(int s){ this.stock = s; }
    public List<OrderEntity> getOrders(){ return orders; }

    public void addOrder(OrderEntity o){
        orders.add(o);
        o.setProduct(this);
    }
}
