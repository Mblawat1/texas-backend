package com.texas.holdem.web;

import com.texas.holdem.elements.players.Winners;
import com.texas.holdem.elements.room.Room;
import com.texas.holdem.service.PlayerService;
import com.texas.holdem.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;

@RestController
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @Autowired
    RoomService roomService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    @Qualifier("threadPoolTaskScheduler")
    TaskScheduler taskScheduler;

    @AllArgsConstructor
    class NewRoundTask implements Runnable {
        Room room;
        @Override
        public void run() {
            roomService.startRound(room);
            messagingTemplate.convertAndSend("/topic/room/" + room.getId(), room);
        }
    }

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
        var room = roomService.getRoomOrThrow(roomId);
        var player = room.getPlayerOrThrow(playerId);

        playerService.setBet(room, player, amount.bet);
        roomService.dealCards(room);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
        var winners = roomService.getWinners(room);
        winners.ifPresent(n -> {
            messagingTemplate.convertAndSend("/topic/room/" + roomId, new Winners("winner", n));
            room.getPlayers().forEach(p -> p.setActive(false));
            taskScheduler.schedule(new NewRoundTask(room),new Date(System.currentTimeMillis() + 5000));
        });

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
        var room = roomService.getRoomOrThrow(roomId);
        var player = room.getPlayerOrThrow(playerId);

        playerService.pass(room, player);
        roomService.dealCards(room);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);

        var winner = roomService.checkAllPassed(room);
        winner.ifPresent(win -> {
            messagingTemplate.convertAndSend("/topic/room/" + roomId, new Winners("winner", Collections.singletonList(win)));
            room.getPlayers().forEach(p -> p.setActive(false));
            taskScheduler.schedule(new NewRoundTask(room),new Date(System.currentTimeMillis() + 5000));
        });

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
        var room = roomService.getRoomOrThrow(roomId);
        var player = room.getPlayerOrThrow(playerId);

        playerService.setReady(room, player);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, room);
        return ResponseEntity.ok().build();
    }

}
