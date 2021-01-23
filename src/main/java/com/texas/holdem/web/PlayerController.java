package com.texas.holdem.web;

import com.texas.holdem.elements.players.Winners;
import com.texas.holdem.service.PlayerService;
import com.texas.holdem.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

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

    /**
     * <h3>Bettowanie</h3>
     *
     * @param roomId   Id pokoju
     * @param playerId Id gracza
     * @param amount   JSON <br/>
     *                 {<br/>
     *                 "bet" : 10<br/>
     *                 }<br/>
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    //zwiękasznie betów
    @PutMapping("/api/room/{roomId}/player/{playerId}")
    public ResponseEntity<?> placeBet(@PathVariable String roomId, @PathVariable int playerId, @RequestBody Amount amount) {
        playerService.setBet(roomId, playerId, amount.bet);
        roomService.dealCards(roomId);
        var winners = roomService.getWinners(roomId);
        winners.ifPresent(n -> messagingTemplate.convertAndSend("/topic/room/" + roomId, new Winners("winner",n)));
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

    /**
     * <h3>Passowanie</h3>
     *
     * @param roomId   Id pokoju
     * @param playerId Id gracza
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    @PutMapping("/api/room/{roomId}/player/{playerId}/pass")
    public ResponseEntity<?> pass(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.pass(roomId, playerId);

        var winner = roomService.checkAllPassed(roomId);
        winner.ifPresent(win ->
                        messagingTemplate.convertAndSend("/topic/room/" + roomId, new Winners("winner",
                                Collections.singletonList(win))));

        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));

        return ResponseEntity.ok().build();
    }

    /**
     * <h3>Zmiana gotowości gracza</h3>
     *
     * @param roomId   Id pokoju
     * @param playerId Id gracza
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    @PutMapping("/api/room/{roomId}/player/{playerId}/ready")
    public ResponseEntity<?> playerReady(@PathVariable String roomId, @PathVariable int playerId) {
        playerService.setReady(roomId, playerId);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().build();
    }

}
