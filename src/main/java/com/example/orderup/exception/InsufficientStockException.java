package com.example.orderup.exception;
public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String msg){ super(msg); }
}
