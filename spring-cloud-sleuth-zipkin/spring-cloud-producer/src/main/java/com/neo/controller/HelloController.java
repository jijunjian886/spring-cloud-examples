package com.neo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/say")
    public String say(String name) {
        System.err.println("I say...");
        /*try {
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return "Hello " + name + "，this is first message";
    }
}