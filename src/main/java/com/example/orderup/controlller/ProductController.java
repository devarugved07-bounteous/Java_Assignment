package com.example.orderup.controlller;

import com.example.orderup.dto.ProductResponseDto;
import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepo;

    public ProductController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody Product p) {
        Product saved = productRepo.save(p);
        return ResponseEntity.ok(mapToDto(saved));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProductResponseDto>> createBatch(@RequestBody List<Product> products) {
        List<Product> savedProducts = productRepo.saveAll(products);
        List<ProductResponseDto> responses = savedProducts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public List<ProductResponseDto> all() {
        return productRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(product -> ResponseEntity.ok(mapToDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ProductResponseDto mapToDto(Product p) {
        return new ProductResponseDto(p.getId(), p.getName(), p.getStock());
    }
}
