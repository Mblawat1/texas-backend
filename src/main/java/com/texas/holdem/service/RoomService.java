package com.texas.holdem.service;

import com.texas.holdem.elements.PlayerDTO;
import com.texas.holdem.elements.Room;
import com.texas.holdem.elements.RoomId;
import com.texas.holdem.elements.Table;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
public class RoomService {
    private HashMap<RoomId, Room> rooms;

    public RoomService() {
        rooms = new HashMap<RoomId, Room>();
    }

    //returnuje room id
    public String createRoom(){
        RoomId id;
        do{
            id = generateId();
        }while(rooms.containsKey(id));

        var room = new Room(id.getId(),new Table(String.valueOf(id),1));
        rooms.put(id,room);
        return id.getId();
    }

    public Optional<Room> getRoom(RoomId id){
        if (rooms.containsKey(id))
            return Optional.of(rooms.get(id));
        return Optional.empty();
    }

    public Optional<Room> deleteRoom(RoomId roomId){
        return Optional.ofNullable(rooms.remove(roomId));
    }

    public HttpStatus addPlayer(RoomId roomId, PlayerDTO playerDTO){
        var optRoom = getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        if (room.getPlayers().size() == 8)
            return HttpStatus.BAD_REQUEST;
        room.addPlayer(playerDTO);
        return HttpStatus.OK;
    }

    private RoomId generateId(){
        var rand = new Random();
        var sb = new StringBuilder();
        for(int i=0;i<4;i++){
            var ch = String.valueOf((char) (rand.nextInt(25)+65));
            sb.append(ch);
        }
        return new RoomId(sb.toString());
    }

    public HttpStatus deletePlayer(RoomId roomId, int playerId) {
        var optRoom = getRoom(roomId);
        if (optRoom.isEmpty())
            return HttpStatus.NOT_FOUND;
        var room = optRoom.get();
        room.deletePlayer(playerId);
        return HttpStatus.OK;
    }
}

