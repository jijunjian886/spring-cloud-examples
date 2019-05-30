package com.neo.web;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Value("${neo.hello}")
    private String hello;

    @Value("${redis.database}")
    private String databaseIndex;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/read")
    public String read() {
        return this.hello;
    }

    @GetMapping("/set/{name}")
    public String set(@PathVariable("name") String name) {
        redisTemplate.opsForValue().set("name", name, 60, TimeUnit.SECONDS);
        return "Redis database " + databaseIndex + ": Set name=" + name + " successfully !";
    }
}