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

    @Autowired
    PlayerService playerService;

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
        private String messageType;
        private String winner;
    }

    /**
     * <h3>Bettowanie</h3>
     * @param roomId Id pokoju
     * @param playerId Id gracza
     * @param amount JSON <br/>
     * {<br/>
     *               "bet" : 10<br/>
     * }<br/>
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    //zwiękasznie betów
    @PutMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> placeBet(@PathVariable String roomId, @PathVariable int playerId, @RequestBody Amount amount) {
        playerService.setBet(roomId, playerId, amount.bet);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

    /**
     * <h3>Passowanie</h3>
     * @param roomId Id pokoju
     * @param playerId Id gracza
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    @PutMapping("/api/room/{roomId}/player/{playerId}/pass")
    public ResponseEntity<?> pass(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.pass(roomId, playerId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));

        var winner = roomService.checkAllPassed(roomId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, new Winner("winner",winner));
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

     /**<h3>Zmiana gotowości gracza</h3>
     * @param roomId Id pokoju
     * @param playerId Id gracza
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    @PutMapping("/api/room/{roomId}/player/{playerId}/ready")
    public ResponseEntity<?> playerReady(@PathVariable String roomId, @PathVariable int playerId){
        playerService.setReady(roomId,playerId);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

}
