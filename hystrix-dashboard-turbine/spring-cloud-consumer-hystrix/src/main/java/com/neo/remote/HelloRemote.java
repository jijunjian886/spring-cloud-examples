package com.neo.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by summer on 2017/5/11.
 */
@FeignClient(name= "spring-cloud-producer", fallback = HelloRemoteHystrix.class)
public interface HelloRemote {
    @GetMapping(value = "/hello/say")
    String say(@RequestParam(value = "name") String name);

    @GetMapping(value = "/hello/say2")
    String say2(@RequestParam(value = "name") String name);
}
