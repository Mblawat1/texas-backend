package com.texas.holdem.web;

import com.texas.holdem.service.PlayerService;
import com.texas.holdem.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class PlayerController {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Amount {
        private int bet;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Winner {
        private String winner;
    }

    @Autowired
    PlayerService playerService;

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //zwiękasznie betów
    @PutMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> placeBet(@PathVariable String roomId, @PathVariable int playerId, @RequestBody Amount amount) {
        playerService.setBet(roomId, playerId, amount.bet);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

    //pasowanie
    @PutMapping("/api/room/{roomId}/player/{playerId}/pass")
    public ResponseEntity<?> pass(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.pass(roomId, playerId);

        var winner = roomService.checkAllPassed(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, new Winner(winner));
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/room/{roomId}/player/{playerId}/ready")
    public ResponseEntity<?> playerReady(@PathVariable String roomId, @PathVariable int playerId){
        playerService.setReady(roomId,playerId);

        return ResponseEntity.ok().build();
    }

}
