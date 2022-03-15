package com.takeaway.game.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Game {
    private int gameId;
    private String player1;
    private boolean player1auto;
    private String player2;
    private boolean player2auto;
    private State state;
    private Turn turn;
    private int currentValue;
    private Move move;

}
