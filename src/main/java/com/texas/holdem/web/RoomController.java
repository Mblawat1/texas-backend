package com.texas.holdem.web;

import com.texas.holdem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;


@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

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

    class Id{
        int id;

        public Id(int id) {
            this.id = id;
        }

        public Id() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    @SendTo("/room/{roomId}/updates")
    public Object sendRoom(@DestinationVariable String roomId, String msg){
        return new SomeResponse(msg);
    }

    //returnuje room id
    @PostMapping("/api/createRoom")
    public ResponseEntity<Id> createRoom(){
        var id = roomService.createRoom();
        return ResponseEntity.status(HttpStatus.CREATED).body(new Id(id));
    }
}
