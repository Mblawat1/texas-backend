package com.texas.holdem.web;

import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.service.PlayerService;
import com.texas.holdem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {

    static class Amount {
        private int bet;

        public Amount(int bet) {
            this.bet = bet;
        }

        public Amount() {
        }

        public int getBet() {
            return bet;
        }

        public void setBet(int bet) {
            this.bet = bet;
        }
    }

    @Autowired
    PlayerService playerService;

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    //dołączenie do pokoju
    @PostMapping("/api/room/{roomId}/player")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody PlayerDTO player) {
        playerService.addPlayer(roomId, player);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoom(roomId));
        return ResponseEntity.ok().build();
    }

    //wyjście z pokoju
    @DeleteMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.deletePlayer(roomId, playerId);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoom(roomId));
        return ResponseEntity.noContent().build();
    }

    //zwiękasznie betów
    @PutMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> placeBet(@PathVariable String roomId, @PathVariable int playerId, @RequestBody Amount amount) {
        playerService.setBet(roomId, playerId, amount.bet);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoom(roomId));
        return ResponseEntity.ok().build();

    }

    //pasowanie
    @PutMapping("/api/room/{roomId}/player/{playerId}/pass")
    public ResponseEntity<?> pass(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.pass(roomId, playerId);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoom(roomId));
        return ResponseEntity.ok().build();
    }

}
