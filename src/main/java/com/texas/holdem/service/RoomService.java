package com.texas.holdem.service;

import com.texas.holdem.elements.Player;
import com.texas.holdem.elements.Room;
import com.texas.holdem.elements.Table;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class RoomService {
    private HashMap<Integer, Room> rooms;

    public RoomService() {
        rooms = new HashMap<Integer, Room>();
    }

    //returnuje room id
    public int createRoom(){
        var id = rooms.size()+1000;
        var room = new Room(String.valueOf(id),new Table(String.valueOf(id),1));
        rooms.put(id,room);
        return id;
    }
}

