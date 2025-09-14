package com.example.orderup.dto;

public class ProductResponseDto {
    private Long id;
    private String name;
    private int stock;

    public ProductResponseDto(Long id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
}
