package com.takeaway.game.service.impl;

import com.takeaway.game.entity.Game;
import com.takeaway.game.entity.Move;
import com.takeaway.game.entity.State;
import com.takeaway.game.entity.Turn;
import com.takeaway.game.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {

    Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    private Map<Integer, Game> games;
    private int lastGameId;

    @Value("${game.settings.maxNumber}")
    private int maximum;

    @PostConstruct
    public void initialize() {
        games = new HashMap<>();
        lastGameId = 0;
    }

    @Override
    public Game joinToGame(String username, boolean auto) {
        if (lastGameId == 0 || games.get(lastGameId).getPlayer2() != null) {
            lastGameId++;
            Game game = new Game();
            game.setGameId(lastGameId);
            game.setPlayer1(username);
            game.setPlayer1auto(auto);
            game.setState(State.WAITING);
            game.setTurn(Turn.PLAYER_2);
            game.setCurrentValue((int) ((Math.random() * (maximum))));
            game.setMove(Move.NEUTRAL);
            games.put(lastGameId, game);

            return game;
        }

        Game game = games.get(lastGameId);
        game.setPlayer2(username);
        game.setPlayer2auto(auto);
        game.setState(State.ONGOING);

        games.put(game.getGameId(), game);
        return game;
    }

    @Override
    public Game getGame(int gameId) {
        return games.get(gameId);
    }

    @Override
    public Game makeMove(int gameId, Move move) {
        Game game = games.get(gameId);

        if (game.getState() == State.PLAYER_1_WON || game.getState() == State.PLAYER_2_WON) {
            return game;
        }

        if (game.getTurn() == Turn.PLAYER_1) {
            if (game.isPlayer1auto()) {
                game = makeAutoMove(game);
            } else {
                game = makeManuelMove(game, move);

                if (game == null) {
                    return null;
                }
            }
            game.setTurn(Turn.PLAYER_2);
            game = checkGameStatus(game, State.PLAYER_1_WON);
        } else {
            if (game.isPlayer2auto()) {
                game = makeAutoMove(game);
            } else {
                game = makeManuelMove(game, move);
                if (game == null) {
                    return null;
                }
            }
            game.setTurn(Turn.PLAYER_1);
            game = checkGameStatus(game, State.PLAYER_2_WON);
        }

        games.put(gameId, game);
        return game;
    }

    private Game makeAutoMove(Game game) {
        int value = game.getCurrentValue();
        int diff = value % 3;

        if (diff == 2) {
            game.setMove(Move.INCREASE);
            game.setCurrentValue((value+1)/3);
            logger.info("new move for gameId:" + game.getGameId() + ". New value: " + ((value+1)/3) + " move: +1");
        } else if (diff == 1) {
            game.setMove(Move.DECREASE);
            game.setCurrentValue((value-1)/3);
            logger.info("new move for gameId:" + game.getGameId() + ". New value: " + ((value-1)/3) + " move: -1");
        } else {
            game.setMove(Move.NEUTRAL);
            game.setCurrentValue(value/3);
            logger.info("new move for gameId:" + game.getGameId() + ". New value: " + (value/3) + " move: 0");
        }

        return game;
    }

    private Game makeManuelMove(Game game, Move move) {
        int value = game.getCurrentValue();
        int diff = value % 3;

        if ((diff == 2 && move == Move.INCREASE) ||
                (diff == 1 && move == Move.DECREASE) ||
                (diff == 0 && move == Move.NEUTRAL)) {
            game = makeAutoMove(game);
        } else {
            game = null;
        }

        return game;
    }

    private Game checkGameStatus(Game game, State player1Won) {
        if (game.getCurrentValue() == 1) {
            game.setState(player1Won);
        }
        return game;
    }

}
