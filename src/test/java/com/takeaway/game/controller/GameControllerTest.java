package com.takeaway.game.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.game.entity.Game;
import com.takeaway.game.entity.Move;
import com.takeaway.game.entity.State;
import com.takeaway.game.entity.Turn;
import com.takeaway.game.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService service;

    private String username1;
    private String username2;
    private Game game;

    @BeforeEach
    public void setup() {
        username1 = "fikret";
        username2 = "kaya";

        game = new Game();
        game.setGameId(1);
        game.setPlayer1(username1);
        game.setPlayer1auto(true);
        game.setState(State.WAITING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(53);
        game.setMove(Move.NEUTRAL);
    }

    @Test
    public void joinGame() throws Exception {
        when(service.joinToGame(any(), anyBoolean())).thenReturn(game);

        MvcResult mvcResult = mockMvc.perform(post("/game/join?userName=kaya&auto=false"))
                .andExpect(status().isOk()).andReturn();

        verify(service, times(1)).joinToGame(any(), anyBoolean());

        then(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(game));
    }

    @Test
    public void getGame() throws Exception {
        when(service.getGame(anyInt())).thenReturn(game);

        MvcResult mvcResult = mockMvc.perform(get("/game/get-game?gameId=1"))
                .andExpect(status().isOk()).andReturn();

        verify(service, times(1)).getGame(anyInt());

        then(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(game));
    }

    @Test
    public void getGameNotFound() throws Exception {
        when(service.getGame(anyInt())).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(get("/game/get-game?gameId=3"))
                .andExpect(status().isNotFound()).andReturn();

        verify(service, times(1)).getGame(anyInt());

        then(mvcResult.getResponse().getContentAsString()).isEqualTo("{\"error\":\"invalid gameId: 3\"}");
    }

    @Test
    public void makeMove() throws Exception {
        game.setPlayer2(username2);
        game.setPlayer2auto(true);
        game.setState(State.ONGOING);
        game.setTurn(Turn.PLAYER_1);
        game.setCurrentValue(18);

        when(service.getGame(anyInt())).thenReturn(game);
        when(service.makeMove(anyInt(), any())).thenReturn(game);

        MvcResult mvcResult = mockMvc.perform(put("/game/move?gameId=1&move=NEUTRAL"))
                .andExpect(status().isOk()).andReturn();

        verify(service, times(1)).getGame(anyInt());
        verify(service, times(1)).makeMove(anyInt(), any());

        then(mvcResult.getResponse().getContentAsString()).isEqualTo(asJsonString(game));
    }

    public static String asJsonString(final Object obj){
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}