package com.texas.holdem.web;

import com.texas.holdem.elements.RoomId;
import com.texas.holdem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
    @SendTo("/room/{roomId}/updates")
    public Object sendRoom(@DestinationVariable String roomId, String msg){
        return new SomeResponse(msg);
    }

    //wysyłanie wiadomości nie w sockecie
    //simpMessagingTemplate.convertAndSend("/room/"+id+"/updates",new SomeResponse("xd"));

    //returnuje room id
    @PostMapping("/api/createRoom")
    public ResponseEntity<RoomId> createRoom(){
        var id = roomService.createRoom();
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoomId(id));
    }

    @GetMapping("/api/room/{roomId}")
    public ResponseEntity<Optional<RoomId>> getRoom(@PathVariable int roomId){
        var res = roomService.getRoom(new RoomId(roomId));
        if (res.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Optional.of(new RoomId(roomId)));
    }
}
