package com.texas.holdem.config;

import com.texas.holdem.elements.room.Room;
import com.texas.holdem.elements.room.RoomId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class BeansConfig {

    /**
     * bean z pokojami
     */
    @Bean
    public HashMap<RoomId, Room> rooms(){
        return new HashMap<>();
    }
}
