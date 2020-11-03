package com.texas.holdem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockController {

    @GetMapping("/*")
    public String mock(){
        return "Some Text";
    }
}
