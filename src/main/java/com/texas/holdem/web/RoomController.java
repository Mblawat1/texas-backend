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


@RestController
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    class SomeResponse {
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

    @MessageMapping("/test")
    @SendTo("/test/return")
    public String send(String str) throws Exception {
        return "Hej " + str;
    }

    // send to musi być inny bo jeśli jest taki sam jak endpoint to klient odbiera też to co wysyła do servera
    @CrossOrigin("*")
    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Object sendRoom(@DestinationVariable String roomId, String msg) {
        return new SomeResponse(msg);
    }

    //wysyłanie wiadomości nie w sockecie
    //simpMessagingTemplate.convertAndSend("/topic/room/"+id,new SomeResponse("xd"));

    //returnuje room id
    @PostMapping("/api/createRoom")
    public ResponseEntity<RoomId> createRoom() {
        String id = roomService.createRoom();
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoomId(id));
    }

    //sprawdzanie czy pokój istnieje, jeśli tak odsyła id, front może subskrybować socket
    @GetMapping("/api/room/{roomId}")
    public ResponseEntity<RoomId> getRoomId(@PathVariable String roomId) {
        var res = roomService.getRoom(roomId);
        if (res.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new RoomId(roomId));
    }

    //usunięcie pokoju
    @DeleteMapping("/api/room/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        var res = roomService.deleteRoom(roomId);
        if (res.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/room/{roomId}/start")
    public ResponseEntity<?> startRound(@PathVariable String roomId) {
        roomService.startRound(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoom(roomId));
        return ResponseEntity.ok().build();
    }

}
