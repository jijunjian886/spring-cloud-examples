package com.neo.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Value("${neo.hello}")
    private String hello;

    @GetMapping("/read")
    public String read() {
        return this.hello;
    }
}