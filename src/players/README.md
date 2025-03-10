<ins>This repository and any of its contributors are NOT affiliated with the copyright, trademarks, or any similar legal reservations that the original King of Tokyo game or its affiliated people may possess, nor are we related to the game itself. This project was made purely as a class project and for educational/learning purposes.</ins>
# Custom Player Documentation
**King of Tokyo App (AOOD-SwingOfTokyo-2024)** <br>
Our King of Tokyo app allows the user to code and use their own custom players. 
Below is the documentation on the needed aspects for a custom players.

1. The custom class ("cc") must start with "Player" (ex. `PlayerClassName`)
2. The cc must be in the `/src/players/` folder (the folder this README is in)
3. The cc must extend the [`Player` class](https://github.com/devsai9/AOOD-SwingOfTokyo-2024/blob/main/src/players/Player.java)
4. The cc must have these methods:
- rerollDice()
  - @param `int currentTurn`
  - @param `int currentPlayer`
  - @param `int inTokyo`
  - @param `int[] dice`
  - @param `int[] playerHealths`
  - @param `int[] playerFames`
  - @return `boolean[6] diceRolls`
- leaveTokyo()
  - @param `int currentTurn`
  - @param `int currentPlayer`
  - @param `int inTokyo`
  - @param `int[] dice`
  - @param `int[] playerHealths`
  - @param `int[] playerFames`
  - @return `boolean leaveTokyo`

 That's all there is to it! <br>
 After coding a cc: clone this repository, put the cc in the `/src/players/` folder, and input `PlayerClassName` into the players table in the app.
