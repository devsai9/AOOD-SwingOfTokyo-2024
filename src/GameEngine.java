import javax.swing.SwingWorker;
import java.util.Arrays;
import players.*;

public class GameEngine {
    private State state = new State();
    private Player[] players;
    private boolean[] deadPlayers;

    private int playersLeft = 0;
    private int turnsInTokyo = 0;
    private SwingGUI.Results logger;

    // SETTINGS
    int outputting;
    int pausing;

    public GameEngine(int numOfPlayers, int numOfGames, int outputtingInterval, int pausing, SwingGUI.Results logger) {
        this.logger = logger;

        players = new Player[numOfPlayers];
        deadPlayers = new boolean[numOfPlayers];

        outputting = outputtingInterval;
        this.pausing = pausing;

        // Run the game in a SwingWorker
        new GameWorker(numOfGames).execute();
    }

    private class GameWorker extends SwingWorker<int[], String> {
        private int numOfGames;

        public GameWorker(int numOfGames) {
            this.numOfGames = numOfGames;
        }

        @Override
        protected int[] doInBackground() {
            int[] results;

            results = runXGames(numOfGames);

            return results;
        }

        @Override
        protected void process(java.util.List<String> chunks) {
            for (String message : chunks) {
                logger.log(message);
            }
        }

        @Override
        protected void done() {
            try {
                int[] results = get();
                for (int j = 0; j < results.length; j++) {
                    logger.log("Player #" + (j + 1) + " (AI) Won: " + (((double) results[j]) / numOfGames * 100.0) + "% of the time (" + results[j] + "/" + numOfGames + " games).");
                }
            } catch (Exception e) {
                logger.log("Error: " + e.getMessage());
            }
        }
    }
    
    private int[] runXGames(int numOfGames) {
        int[] results = new int[players.length];

        for (int ijk = 0; ijk < results.length; ijk++) {
            results[ijk] = 0;
        }
        
        for (int i = 0; i < numOfGames; i++) {
            state.setInTokyo(-1);

            deadPlayers = new boolean[players.length];
            
            for (int j = 0; j < players.length; j++) {
                players[j] = new PlayerNaive();
                players[j].setId(j);
                deadPlayers[j] = false;
            }
            
            playersLeft = players.length;
            
            int[] tempH = new int[players.length];
            int[] tempF = new int[players.length];
            for (int h = 0; h < players.length; h++) {
                tempH[h] = 10;
                tempF[h] = 0;
            }
            
            state.setPlayerHealths(tempH);
            state.setPlayerFames(tempF);
    
            state.setCurrentPlayer((int) Math.floor(Math.random() * players.length));
            int res = runGame(i + 1);
            results[res]++;

            if (pausing <= GameIntervals.PER_GAME) pause(1000);
        }

        // There will always be at least overall status reporting; no check needed
        if (outputting <= GameIntervals.PER_GAME) logger.log("\n");
//        for (int j = 0; j < results.length; j++) {
//            logger.log("Player #" + (j+1) + " (AI) Won: " + (((double) results[j]) / numOfGames * 100.0) + "% of the time (" + results[j] + "/" + numOfGames + " games).");
//        }

        return results;
    }

    private void setFameHelper(int player, int deltaFame) {
        if (player == -1) return;
        if (state.getPlayerFames()[player] + deltaFame <= 20) {
            int[] playerFamesTemp = state.getPlayerFames();
            playerFamesTemp[player] += deltaFame;
            state.setPlayerFames(playerFamesTemp);
            
            players[player].setFame(playerFamesTemp[player] + deltaFame);
        } else {
            int[] playerFamesTemp = state.getPlayerFames();
            playerFamesTemp[player] = 20;
            state.setPlayerFames(playerFamesTemp);
            
            players[player].setFame(20);
        }
    }

    private void setHealthHelper(int player, int deltaHealth) {
        if (player == -1) return;
        if (state.getPlayerHealths()[player] + deltaHealth > 12) {
            // Healths tries to go above 12
            int[] playerHealthsTemp = state.getPlayerHealths();
            playerHealthsTemp[player] = 12;
            state.setPlayerHealths(playerHealthsTemp);
            
            players[player].setHealth(12);
        } else if (state.getPlayerHealths()[player] + deltaHealth < 0) {
            // Health tries to go below 0
            int[] playerHealthsTemp = state.getPlayerHealths();
            playerHealthsTemp[player] = 0;
            state.setPlayerHealths(playerHealthsTemp);
            
            players[player].setHealth(0);
        } else {
            int[] playerHealthsTemp = state.getPlayerHealths();
            playerHealthsTemp[player] += deltaHealth;
            state.setPlayerHealths(playerHealthsTemp);
            
            players[player].setHealth(playerHealthsTemp[player] + deltaHealth);
        }
        
    }

    private void updateTokyoStarter() {
        state.setInTokyo(state.getCurrentPlayer());
        setFameHelper(state.getCurrentPlayer(), 1);
    }

    private void pause(int a) {
        try {
            Thread.sleep(a);
        }
        catch (Exception e) {}
    }
    
    private boolean contains(int num, int[] playerFames) {
        for (int i = 0; i<playerFames.length; i++) {
            if (playerFames[i] == num) {
                return true;
            }
        }
        return false;
    }

    private int runGame(int round) {
        // Start of game
        int numHavePlayed = 0;
        while (playersLeft > 1 && !contains(20, state.getPlayerFames())) {
            // For each player...
            if (state.getPlayerHealths()[state.getCurrentPlayer()] == 0) {
                // Player is dead
                if (pausing == GameIntervals.PER_TURN) pause(500);
                if (outputting == GameIntervals.PER_TURN) {
                    logger.log("———————New Turn———————\n \nPlayer #" + (state.getCurrentPlayer() + 1) + ": Dead");
                }
                
                // Increasing the health of the person who killed it
                if (deadPlayers[state.getCurrentPlayer()] == false) {
                    if (state.getCurrentPlayer() == 0) {
                        for (int i = players.length - 1; i >= 0; i--) {
                            if (!deadPlayers[i]) {
                                setFameHelper(players.length - 1, 1);
                            }
                        }
                    } else {
                        setFameHelper(state.getCurrentPlayer() - 1, 1);
                    }
                }
                deadPlayers[state.getCurrentPlayer()] = true;
                
                state.setCurrentPlayer(state.getCurrentPlayer() + 1);
                
                if (state.getCurrentPlayer() >= players.length) state.setCurrentPlayer(0);
                numHavePlayed++;

                playersLeft = state.getPlayerHealths().length;
                for (int ijkl = 0; ijkl < state.getPlayerHealths().length; ijkl++) {
                    if (state.getPlayerHealths()[ijkl] == 0) playersLeft--;
                }

                continue;
            }

            if (state.getPlayerHealths()[state.getCurrentPlayer()] > 0) {

                // Checking to see if survived a full round
                if (state.getInTokyo() == state.getCurrentPlayer() && turnsInTokyo != 0) {
                    setFameHelper(state.getInTokyo(), 2);
                }

                // Only prints the data and roll if the player is still alive
                if (pausing == GameIntervals.PER_TURN) pause(500);
                if (outputting == GameIntervals.PER_TURN) logger.log("———————New Turn———————\n \nPlayer #" + (state.getCurrentPlayer() + 1) + ": \nHealth: " + state.getPlayerHealths()[state.getCurrentPlayer()] + "\nFame: " + state.getPlayerFames()[state.getCurrentPlayer()]);
                
                if (numHavePlayed != 0 && outputting == GameIntervals.PER_TURN) logger.log("Player #" + (state.getInTokyo() + 1) + " is in Tokyo");

                // Checking to see if player wants to leave tokyo
                if (state.getCurrentPlayer() == state.getInTokyo() && players[state.getCurrentPlayer()].leaveTokyo(state.getCurrentTurn(), state.getCurrentPlayer(), state.getInTokyo(), state.getDice(), state.getPlayerHealths(), state.getPlayerFames())) {
                    // Player wants to leave tokyo
                    // Changing who is in tokyo
                    if (playersLeft == 1) break;
                    boolean temp_valid = false;
                    while (!temp_valid) {
                        state.setInTokyo(state.getInTokyo() - 1);
                        if (state.getInTokyo() == -1) {
                            for (int i = players.length - 1; i >= 0; i--) {
                                if (!deadPlayers[i]) {
                                    state.setInTokyo(players.length - 1);
                                }
                            }
                        }
                        if (state.getPlayerHealths()[state.getInTokyo()] != 0) temp_valid = true;
                    }

                    // increasing the fame of the new person in tokyo for scaring the person in tokyo
                    setFameHelper(state.getInTokyo(), 1);

                    turnsInTokyo = 0;
                }

                // Player survived a full round in tokyo

                // Keep track of dice
                int[] userDiceRoll = new int[6];
                for (int i = 0; i < userDiceRoll.length; i++) {
                    userDiceRoll[i] = ((int) Math.floor(Math.random() * 6)) + 1;
                }

                userDiceRoll = rollDice(userDiceRoll);

                // If player in tokyo dies then current player goes in
                if (state.getInTokyo() > -1 && state.getPlayerHealths()[state.getInTokyo()] == 0) {
                    state.setInTokyo(state.getCurrentPlayer());
                    setFameHelper(state.getCurrentPlayer(), 1);
                }

                if (outputting == GameIntervals.PER_TURN) {
                    StringBuilder temp = new StringBuilder("Final dice roll: ");

                    for (int finalDiceRoll1 : userDiceRoll) {
                        temp.append(finalDiceRoll1).append(" ");
                    }

                    logger.log(temp.toString());
                }
                
                // Increasing the current turn
                state.setCurrentTurn(state.getCurrentTurn() + 1);
                
                boolean extraTurn = processDice(userDiceRoll);
                
                if (extraTurn) {
                    // Increasing the current turn
                    // state.setCurrentTurn(state.getCurrentTurn() + 1);
                    
                    for (int i = 0; i < userDiceRoll.length; i++) {
                        userDiceRoll[i] = ((int) Math.floor(Math.random() * 6)) + 1;
                    }

                    userDiceRoll = rollDice(userDiceRoll);
                    processDice(userDiceRoll);
                    if (outputting == GameIntervals.PER_TURN) {
                        StringBuilder temp = new StringBuilder("Second final dice roll: ");

                        for (int finalDiceRollTwo : userDiceRoll) {
                            temp.append(finalDiceRollTwo).append(" ");
                        }

                        logger.log(temp.toString());
                    }
                }
            }

            if (numHavePlayed == 0) updateTokyoStarter();
            state.setCurrentPlayer(state.getCurrentPlayer() + 1);
            if (state.getCurrentPlayer() >= players.length) state.setCurrentPlayer(0);
            numHavePlayed++;
            turnsInTokyo++;
        }

        if (playersLeft == 1) {
            for (int j = 0; j < state.getPlayerHealths().length; j++) {
                if (state.getPlayerHealths()[j] != 0) {
                    if (outputting <= GameIntervals.PER_GAME) logger.log((outputting == GameIntervals.PER_TURN ? "\n" : "") + "Round #" + round + ": Player #" + (j + 1) + " has won!");
                    return j;
                }
            }
        } else {
            for (int index = 0; index < state.getPlayerFames().length; index++) {
                if (state.getPlayerFames()[index] == 20) {
                    if (outputting <= GameIntervals.PER_GAME) logger.log((outputting == GameIntervals.PER_TURN ? "\n" : "") + "Round #" + round + ": Player #" + (index + 1) + " has won!");
                    return index;
                }
            }
        }
        
        return -1;
    }

    // runGame() abstractions
    // Handle dice
    private int[] rollDice(int[] userDiceRoll) {
        // User has two chances to re-roll
        state.setDice(userDiceRoll);
        for (int diceRolls = 1; diceRolls <= 2; diceRolls++) {
            boolean[] userChoice = players[state.getCurrentPlayer()].rerollDice(state.getCurrentTurn(), state.getCurrentPlayer(), state.getInTokyo(), state.getDice(), state.getPlayerHealths(), state.getPlayerFames());

            if (Arrays.equals(userChoice, new boolean[]{false, false, false, false, false, false})) {
                break;
            }

            for (int index = 0; index < userChoice.length; index++) {
                // if the user wants to reroll the dice then reroll
                if (userChoice[index]) {
                    userDiceRoll[index] = ((int) Math.floor(Math.random() * 6)) + 1;
                }
            }
            state.setDice(userDiceRoll);

        }

        return userDiceRoll;
    }

    private boolean processDice(int[] userDiceRoll) {
        int[] numOfDice = new int[]{0, 0, 0, 0, 0, 0};
        for (int dice : userDiceRoll) {
            numOfDice[dice - 1] += 1;

            if (dice == 5) {
                if (state.getCurrentPlayer() != state.getInTokyo()) {
                    setHealthHelper(state.getCurrentPlayer(), 1);
                }
            } else if (dice == 6) {
                if (state.getInTokyo() == -1) continue;
                if (state.getCurrentPlayer() != state.getInTokyo()) {
                    // Current player not in Tokyo: Attack the monster in Tokyo
                    // Death of player in Tokyo is already checked for
                    setHealthHelper(state.getInTokyo(), -1);
                    if (state.getInTokyo() >= 0 && state.getPlayerHealths()[state.getInTokyo()] != 0) {

                        int[] abc = new int[2];
                        abc[0] = state.getCurrentPlayer();
                        abc[1] = state.getInTokyo();

                        state.setCurrentPlayer(abc[1]);
                        // Prompt currentPlayer if they would like to leave Tokyo
//                        if (players[state.getInTokyo()] instanceof PlayerHuman && outputting) {
//                            System.out.print("\n" + "Player #" + (state.getInTokyo() + 1) + ": ");
//                        }
                        boolean playerAchoosesToStay = players[abc[1]].leaveTokyo(state.getCurrentTurn(), state.getCurrentPlayer(), state.getInTokyo(), state.getDice(), state.getPlayerHealths(), state.getPlayerFames());
                        if (playerAchoosesToStay) {
                            // Player A chooses to stay, so switch back
                            state.setCurrentPlayer(abc[0]);
                            break;
                        } else {
                            // Player A chooses to leave, so update inTokyo and currentPlayer
                            state.setInTokyo(abc[0]);
                            state.setCurrentPlayer(abc[1]);
                            break;
                        }
                    }

                } else {
                    // Current player is in Tokyo: Attack all other monsters
                    for (int j = 0; j < players.length; j++) {
                        if (j == state.getInTokyo()) continue;
                        setHealthHelper(j, -1);

                        // If a player dies: Recalculate how many players are left
                        if (state.getPlayerHealths()[j] == 0) {
                            playersLeft = state.getPlayerHealths().length;
                            for (int ijkl = 0; ijkl < state.getPlayerHealths().length; ijkl++) {
                                if (state.getPlayerHealths()[ijkl] == 0) playersLeft--;
                            }
                        }
                    }
                }
            }
        }

        // increases the fame if rolled 1, 2, 3
        for (int index = 1; index <= 3; index++) {
            if (numOfDice[index - 1] >= 3) {
                //fame doesn't go into negatives anymore
                setFameHelper(state.getCurrentPlayer(), index + (numOfDice[index - 1] - 3));
            }
        }

        // if (numOfDice[3] >= 3) return true | else return false;
        return numOfDice[3] >= 3;
    }
}