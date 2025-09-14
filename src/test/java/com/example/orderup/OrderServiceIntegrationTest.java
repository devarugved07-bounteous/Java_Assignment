package com.example.orderup;

import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;
import com.example.orderup.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceIntegrationTest {
    @Autowired ProductRepository productRepo;
    @Autowired OrderService orderService;

    @Test
    void concurrentOnlyOneSucceeds() throws Exception{
        productRepo.deleteAll();
        Product p=productRepo.save(new Product("Rare",1));

        ExecutorService ex=Executors.newFixedThreadPool(2);
        Callable<Boolean> task=()->{
            try{ orderService.placeOrder(p.getId(),1); return true;}
            catch(Exception e){ return false;}
        };

        var results=ex.invokeAll(java.util.List.of(task,task));
        ex.shutdown();
        int success=0;
        for(var f:results) if(f.get()) success++;
        assertEquals(1,success);
    }
}
