package com.texas.holdem.elements.room;

import com.texas.holdem.elements.players.PlayerDTO;
import com.texas.holdem.service.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    Room room;

    @BeforeEach
    void init(){
        room = new Room("ABCD", new Table());
    }

    @Test
    void shouldAddPlayer(){
        var player = new PlayerDTO("andrzej", 3);

        room.addPlayer(player);

        Assertions.assertEquals(room.getPlayers().size(),1);
    }

    @Test
    void shouldThrowExceptionr(){
        var player = new PlayerDTO("andrzej", 30);

        Assertions.assertThrows(ResponseStatusException.class,() -> room.addPlayer(player));
    }
}