package com.texas.holdem.web;

import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.RoomId;
import com.texas.holdem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //dołączenie do pokoju
    @PostMapping("/api/room/{roomId}/player")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody PlayerDTO player){
        var res = roomService.addPlayer(new RoomId(roomId),player);
        if (res == HttpStatus.OK){
            simpMessagingTemplate.convertAndSend("/topic/room/"+roomId,roomService.getRoom(new RoomId(roomId)));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(res).build();
    }

    //wyjście z pokoju
    @DeleteMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId, @PathVariable int playerId){
        var res = roomService.deletePlayer(new RoomId(roomId), playerId);
        if (res == HttpStatus.OK){
            simpMessagingTemplate.convertAndSend("/topic/room/"+roomId,roomService.getRoom(new RoomId(roomId)));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(res).build();
    }
}
