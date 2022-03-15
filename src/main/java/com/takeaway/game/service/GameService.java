package com.takeaway.game.service;

import com.takeaway.game.entity.Game;
import com.takeaway.game.entity.Move;

public interface GameService {

    Game joinToGame(String username, boolean auto);
    Game getGame(int gameId);
    Game makeMove(int gameId, Move move);
}
