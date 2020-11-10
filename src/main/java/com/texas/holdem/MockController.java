package com.texas.holdem;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
public class MockController {

    class SomeResponse{
        private String text;

        public SomeResponse(String text) {
            this.text = text;
        }

        public SomeResponse() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @CrossOrigin("*")
    @MessageMapping("/test")
    @SendTo("/test")
    public SomeResponse send(String str)throws Exception{
        return new SomeResponse(str);
    }
}
