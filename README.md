#Game of Three Application:
* JAVA 11
* Spring Boot 2.6.4
* Maven
* Docker

##Request Endpoins:
* POST: "http://localhost:8080/game/join?userName=fiko&auto=true" : create a new game if there is no available game is found, otherwise join to a game with username and ai (play manuel or auto)
* GET: "http://localhost:8080/game/get-game?gameId=1" : get game details with given gameId
* PUT: "http://localhost:8080/game/move?gameId=1&move=INCREASE" : make a new move, if user mode is auto then move param is ignored

##Docker Run Commands:
In order to run the application using Docker, following docker commands can be used:
* docker build -t got-game .
* docker run -d -p 8080:8080 got-game

#Notes
* In configuration file (application.yml) specify the maxValue to be generated randomly
* Requests return either relevant responses (if they are valid) or error messages
* Move enum: {INCREASE, NEUTRAL, DECREASE} (+1, 0, -1)
* State enum: {WAITING, ONGOING, PLAYER_1_WON, PLAYER_2_WON} 
  * WAITING: when user1 waits user2 to join
  * ONGOING: users playing the game
  * PLAYER_1_WON: game ended and user1 won the game
  * PLAYER_2_WON: game ended and user2 won the game
* Turn enum: {PLAYER_1, PLAYER_2} (which player's turn it is) 
* Following is sample output of game object:
```json
  {
  "gameId": 1,
  "player1": "fikret",
  "player1auto": true,
  "player2": "kaya",
  "player2auto": true,
  "state": "ONGOING",
  "turn": "PLAYER_2",
  "currentValue": 17,
  "move": "NEUTRAL"
  }
```