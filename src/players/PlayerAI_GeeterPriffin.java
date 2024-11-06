package players;

public class PlayerAI_GeeterPriffin extends Player {
    public boolean[] rerollDice(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames) {
        
        boolean[] output = new boolean[] {false, false, false, false, false, false};
        
        int[] numOfDice = new int[6]; 
        
        for (int d : dice) {
            numOfDice[d - 1] += 1;
        }
        
        // Dice left to reroll: Updated for necessary things
        int diceLeft = 6;
        
        for (int j = 0; j < numOfDice.length; j++) {
            // 1: Ignore :(
            if (dice[j] == 1) {
                if (numOfDice[0] >= 4 && playerHealths[getId()] <= 4 && playerFames[getId()] >= 12) {
                    output[j] = false;
                } else {
                    output[j] = true;
                }
            }
            
            // 2
            if (dice[j] == 2) {
                if (numOfDice[1] >= 2) {
                    output[j] = false;
                } else {
                    output[j] = true;
                }
            }
            
            // 3
            if (dice[j] == 3) {
                if (numOfDice[2] >= 2) {
                    output[j] = false;
                } else {
                    output[j] = true;
                }
            }
            
            // 4
            if (dice[j] == 4 && numOfDice[3] >= 2) {
                if (getCurrentIndexOfNumber(j, 4, dice) > 3) {
                    // Don't keep the four if you already have 3 fours
                    output[j] = true;
                } else {
                    output[j] = false;
                }
            }
            
            // 5
            if (dice[j] == 5) {
                if (getId() == inTokyo) {
                    // In Tokyo: Can't heal
                    output[j] = true;
                } else {
                    // Not in tokyo
                    if (playerHealths[getId()] <= 5) {
                        output[j] = false;
                        diceLeft--;
                        continue;
                    }
                    output[j] = true;
                }
            }
            
            // 6
            if (dice[j] == 6) {
                output[j] = false;
                diceLeft--;
            }
        }
        
        
        for (int index = 0; index < dice.length; index++) {
            if (playerHealths[getId()] <= 4) {
                // Prioritize health over fame
                if (dice[index] == 1 || dice[index] == 2 || dice[index] == 3) {
                    output[index] = true;
                }
            }
            
            if (diceLeft <= 2) {
                // If no chance to get 3 of a certain fame the reroll those
                if (dice[index] == 1 || dice[index] == 2 || dice[index] == 3) {
                    output[index] = true;
                }
            }
            
            if (numOfDice[3] == 2) {
                // trying to get a third four
                if (numOfDice[0] == 0 && numOfDice[1] == 0 && numOfDice[2] < 2 ) {
                    //reroll the three
                    if (dice[index] == 3) output[index] = true;
                    
                } else if (numOfDice[0] == 0 &&  numOfDice[1] < 2) {
                    //reroll the two
                    if (dice[index] == 2) output[index] = true;
                    
                } else if (numOfDice[0] < 4) {
                    //reroll the one
                    if (dice[index] == 1) output[index] = true;
                    
                } else if (getCurrentIndexOfNumber(index, 5, dice) == 1) {
                    // only rerolls the first five
                    if (dice[index] == 5) output[index] = true;
                }
            }
            
            // roll 2 twos 2 threes 2 fours
            if (numOfDice[1] == 2 && numOfDice[2] == 2 && numOfDice[3] == 3) {
                //keep everything but the twos
                if (dice[index] == 2) {
                    output[index] = true;
                }
            }
        }
        
//        for (int kraftmacncheese = 0; kraftmacncheese < dice.length; kraftmacncheese++) {
//            System.out.print(dice[kraftmacncheese]);
//            System.out.print(output[kraftmacncheese]);
//        }
//        System.out.println();

        return output;
    }
    
    private int getCurrentIndexOfNumber(int index, int target, int[] dice) {
        if (target < 0 || target > 6) throw new IllegalArgumentException("Target Invalid");
        if (index < 0 || index > dice.length - 1) throw new IllegalArgumentException("Index invalid");
        // if (dice[index] != target) return -1;
        
        int numOfTarget = 0;
        for (int i = 0; i <= index; i++) {
            if (dice[i] == target) numOfTarget++;
        }
        
        return numOfTarget;
    }
    
    public boolean leaveTokyo(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames) {
        // Assumed that inTokyo is an ID of an instance of PlayerAI
        
        if (playerHealths[inTokyo] <= 5 && playerFames[inTokyo] <= 17) {
            return true;
        }
        
        // Don't leave tokyo if health greater than 5 or fame greater than 17
        return false;
    }
}