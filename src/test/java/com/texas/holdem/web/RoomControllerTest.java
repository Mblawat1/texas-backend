package com.texas.holdem.web;

import com.texas.holdem.elements.room.RoomId;
import com.texas.holdem.service.RoomService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(RoomController.class)
class RoomControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    RoomService mockService;
    @MockBean
    SimpMessagingTemplate messagingTemplate;


    @Test
    void shouldReturnRoomId() throws Exception {
        Mockito.when(mockService.createRoom()).thenReturn("FFFF");

        mvc.perform(post("/api/createRoom"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",is("FFFF")));


    }
}