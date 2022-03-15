package com.takeaway.game.service;

import com.takeaway.game.entity.Game;
import com.takeaway.game.entity.Move;
import com.takeaway.game.entity.State;
import com.takeaway.game.entity.Turn;
import com.takeaway.game.service.impl.GameServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
class GameServiceImplTest {
    @InjectMocks
    private GameServiceImpl service;

    private Map<Integer, Game> games;
    private int lastGameId;

    @BeforeEach
    public void setup() {
        games = new HashMap<>();
        lastGameId = 0;
    }

    @Test
    public void joinToGameAsPlayer1() {
        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        String username = "fikret";
        Game game = service.joinToGame(username, true);
        assertEquals(username, game.getPlayer1());
    }

    @Test
    public void joinToGameAsPlayer2() {
        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        String username1 = "fikret";
        service.joinToGame(username1, true);
        String username2 = "kaya";
        Game game = service.joinToGame(username2, true);

        assertEquals(username2, game.getPlayer2());
        assertEquals(State.ONGOING, game.getState());
    }

    @Test
    public void getGame() {
        String username = "fikret";
        boolean auto = true;

        Game game = new Game();
        game.setGameId(1);
        game.setPlayer1(username);
        game.setPlayer1auto(auto);
        game.setState(State.WAITING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(53);
        game.setMove(Move.NEUTRAL);
        lastGameId++;
        games.put(lastGameId, game);

        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game retrievedGame = service.getGame(1);

        assertEquals(username, retrievedGame.getPlayer1());
    }

    @Test
    public void getGameNull() {
        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game game = service.getGame(1);

        assertNull(game);
    }

    @Test
    public void makeAutoMove() {
        String username1 = "fikret";
        String username2 = "kaya";
        boolean auto = true;
        int randomNumber = 53;
        int expectedNumber = 18;

        Game game = new Game();
        game.setGameId(1);
        game.setPlayer1(username1);
        game.setPlayer1auto(auto);
        game.setPlayer2(username2);
        game.setPlayer2auto(auto);
        game.setState(State.ONGOING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(randomNumber);
        game.setMove(Move.NEUTRAL);
        lastGameId++;
        games.put(lastGameId, game);

        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game retrievedGame = service.makeMove(1, Move.NEUTRAL);

        assertEquals(expectedNumber, retrievedGame.getCurrentValue());
    }

    @Test
    public void makeManuelMove() {
        String username1 = "fikret";
        String username2 = "kaya";
        boolean auto = false;
        int randomNumber = 53;
        int expectedNumber = 18;

        Game game = new Game();
        game.setGameId(1);
        game.setPlayer1(username1);
        game.setPlayer1auto(auto);
        game.setPlayer2(username2);
        game.setPlayer2auto(auto);
        game.setState(State.ONGOING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(randomNumber);
        game.setMove(Move.NEUTRAL);
        lastGameId++;
        games.put(lastGameId, game);

        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game retrievedGame = service.makeMove(1, Move.INCREASE);

        assertEquals(expectedNumber, retrievedGame.getCurrentValue());
    }

    @Test
    public void makeManuelMoveInvalidMove() {
        String username1 = "fikret";
        String username2 = "kaya";
        boolean auto = false;
        int randomNumber = 53;

        Game game = new Game();
        game.setGameId(1);
        game.setPlayer1(username1);
        game.setPlayer1auto(auto);
        game.setPlayer2(username2);
        game.setPlayer2auto(auto);
        game.setState(State.ONGOING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(randomNumber);
        game.setMove(Move.NEUTRAL);
        lastGameId++;
        games.put(lastGameId, game);

        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game retrievedGame = service.makeMove(1, Move.DECREASE);

        assertNull(retrievedGame);
    }

    @Test
    public void makeAutoMoveEndOfGame() {
        String username1 = "fikret";
        String username2 = "kaya";
        boolean auto = true;
        int randomNumber = 4;

        Game game = new Game();
        game.setGameId(1);
        game.setPlayer1(username1);
        game.setPlayer1auto(auto);
        game.setPlayer2(username2);
        game.setPlayer2auto(auto);
        game.setState(State.ONGOING);
        game.setTurn(Turn.PLAYER_2);
        game.setCurrentValue(randomNumber);
        game.setMove(Move.NEUTRAL);
        lastGameId++;
        games.put(lastGameId, game);

        ReflectionTestUtils.setField(service, "games", games);
        ReflectionTestUtils.setField(service, "lastGameId", lastGameId);

        Game retrievedGame = service.makeMove(1, Move.NEUTRAL);

        assertEquals(State.PLAYER_2_WON, retrievedGame.getState());
    }

}