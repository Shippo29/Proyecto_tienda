package com.example.envios.envios_service.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() { super(); }
    public ResourceNotFoundException(String message) { super(message); }
    public ResourceNotFoundException(String message, Throwable cause) { super(message, cause); }
}
