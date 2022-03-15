package com.takeaway.game.controller;

import com.takeaway.game.entity.Game;
import com.takeaway.game.entity.Move;
import com.takeaway.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    @PostMapping("join")
    public ResponseEntity<Object> joinGame(@RequestParam String userName, @RequestParam Boolean auto) {
        if (!userName.isEmpty()) {
            Game game = gameService.joinToGame(userName, auto);
            logger.info("username: " + userName + " joined to game");
            return ResponseEntity.ok(game);
        }

        JSONObject obj = new JSONObject();
        obj.put("error", "invalid username: " + userName);
        logger.error("cannot join game, invalid username: " + userName);
        return ResponseEntity.badRequest().body(obj);
    }

    @GetMapping("get-game")
    public ResponseEntity<Object> getGame(@RequestParam Integer gameId) {
        Game game = gameService.getGame(gameId);
        if (game != null) {
            logger.info("game retrieved, gameId: " + gameId);
            return ResponseEntity.ok(game);
        }

        JSONObject obj = new JSONObject();
        obj.put("error", "invalid gameId: " + gameId);
        logger.error("cannot get game, invalid gameId: " + gameId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(obj);
    }

    @PutMapping("move")
    public ResponseEntity<Object> makeMove(@RequestParam int gameId, @RequestParam Move move) {
        Game game = gameService.getGame(gameId);

        JSONObject obj = new JSONObject();
        if (game == null) {
            obj.put("error", "invalid gameId: " + gameId);
            logger.error("cannot get game, invalid gameId: " + gameId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(obj);
        }

        if (game.getCurrentValue() == 1) {
            logger.warn("move not processed because game is already done, gameId: " + gameId);
            return ResponseEntity.ok(game);
        }

        game = gameService.makeMove(gameId, move);

        if (game != null) {
            return ResponseEntity.ok(game);
        }

        obj.put("error", "invalid move: " + move);
        logger.error("cannot make move, gameId: " + gameId + " - move: " + move.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(obj);
    }
}
