package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringBootHelloWorld {
    public static void main(String[] args){

    }

    @GetMapping("/x")
    public String hello(){
        return "Hey, Spring Boot 的 ! Hello World ! ";
    }

    @GetMapping("/index")
    public String helloIndex(){
        return "index";
    }
}
