package com.demoapp.demoapp.services;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    /**
     * Returns a simple greeting message.
     *
     * @return a {@link String} containing "Hello World!"
     */
    public String sayHello() {
        return "Hello World!";
    }
}
