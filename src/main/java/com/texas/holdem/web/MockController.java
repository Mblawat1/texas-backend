package com.texas.holdem.web;

import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    @SendTo("/test/return")
    public String send(String str)throws Exception{
        return "Hej " + str;
    }

    // send to musi być inny bo jeśli jest taki sam jak endpoint to klient odbiera też to co wysyła do servera
    @CrossOrigin("*")
    @MessageMapping("/room/{roomId}")
    @SendTo("/room/{roomId}/return")
    public String sendRoom(@DestinationVariable String roomId, String msg){
        return "Pokój " + roomId + " msg: " + msg;
    }
}
