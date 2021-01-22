package com.texas.holdem.web;

import com.texas.holdem.elements.players.PlayerDTO;
import com.texas.holdem.elements.room.RoomId;
import com.texas.holdem.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private SimpMessagingTemplate messagingTemplate;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Id {
        private int id;
    }

    // send to musi być inny bo jeśli jest taki sam jak endpoint to klient odbiera też to co wysyła do servera
    @CrossOrigin("*")
    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Object sendRoom(@DestinationVariable String roomId, String msg) {
        return msg;
    }

    //wysyłanie wiadomości nie w sockecie
    //simpMessagingTemplate.convertAndSend("/topic/room/"+id,new SomeResponse("xd"));

    /**
     * <h3>Tworzenie nowego pokoju</h3>
     * @return Id utworzonego pokoju
     */
    @PostMapping("/api/createRoom")
    public ResponseEntity<RoomId> createRoom() {
        String id = roomService.createRoom();
        return ResponseEntity.status(HttpStatus.CREATED).body(new RoomId(id));
    }

    /**
     * <h3>Sprawdzenie czy pokój istnieje</h3>
     * @param roomId Id pokoju
     * @return Id pokoju jeśli istnieje, jeśli nie status 404
     */
    @GetMapping("/api/room/{roomId}")
    public ResponseEntity<RoomId> getRoomId(@PathVariable String roomId) {
        var res = roomService.getRoomOrThrow(roomId);
        return ResponseEntity.ok(new RoomId(roomId));
    }

    //usunięcie pokoju
//    @DeleteMapping("/api/room/{roomId}")
//    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
//        var res = roomService.deleteRoom(roomId);
//        if (res.isEmpty())
//            return ResponseEntity.notFound().build();
//        return ResponseEntity.noContent().build();
//    }

//    @GetMapping("/api/room/{roomId}/start")
//    public ResponseEntity<?> startRound(@PathVariable String roomId) {
//        roomService.startRound(roomId);
//        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
//        return ResponseEntity.ok().build();
//    }

    /**
     * <h3>Dołączanie do pokoju</h3>
     * @param roomId Id pokoju
     * @param player Json<br/>
     *               {<br/>
     *                   "nickname" : "andrzej",<br/>
     *                   "avatar" : 13<br/>
     *               }
     * @return Id utworzonego gracza, błędy jeśli coś nie zadziałało
     */
    @PostMapping("/api/room/{roomId}/player")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId, @RequestBody PlayerDTO player) {
        var id = roomService.addPlayer(roomId, player);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));
        return ResponseEntity.ok().body(new Id(id));
    }

    /**
     * <h3>Wyjście z pokoju</h3>
     * @param roomId Id pokoju
     * @param playerId Id gracza
     * @return HttpStatus.OK jeśli wszystko ok, inne błędy jeśli coś nie zadziałało
     */
    @CrossOrigin("*")
    @PostMapping("/api/room/{roomId}/player/{playerId}/delete")
    public ResponseEntity<?> leaveRoom(@PathVariable String roomId, @PathVariable int playerId) {
        roomService.deletePlayer(roomId, playerId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomService.getRoomOrThrow(roomId));

        var playersInRoom = roomService.getRoomOrThrow(roomId).getPlayers().size();
        if (playersInRoom == 0)
            roomService.deleteRoom(roomId);

        return ResponseEntity.noContent().build();
    }

}
