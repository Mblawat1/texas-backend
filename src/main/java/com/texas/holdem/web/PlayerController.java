package com.texas.holdem.web;

import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.RoomId;
import com.texas.holdem.service.PlayerService;
import com.texas.holdem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerController {

    static class Amount{
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
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody PlayerDTO player){
        var res = playerService.addPlayer(new RoomId(roomId),player);
        if (res == HttpStatus.OK){
            simpMessagingTemplate.convertAndSend("/topic/room/"+roomId,roomService.getRoom(new RoomId(roomId)));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(res).build();
    }

    //wyjście z pokoju
    @DeleteMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId, @PathVariable int playerId){
        var res = playerService.deletePlayer(new RoomId(roomId), playerId);
        if (res == HttpStatus.OK){
            simpMessagingTemplate.convertAndSend("/topic/room/"+roomId,roomService.getRoom(new RoomId(roomId)));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(res).build();
    }

    @PutMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> placeBet(@PathVariable String roomId, @PathVariable int playerId,@RequestBody Amount amount){
        var res = playerService.setBet(new RoomId(roomId), playerId, amount.bet);
        if (res == HttpStatus.OK){
            simpMessagingTemplate.convertAndSend("/topic/room/"+roomId,roomService.getRoom(new RoomId(roomId)));
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(res).build();
    }
}
