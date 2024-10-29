package players;

public class PlayerNaive extends Player {
    public boolean[] rerollDice(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames) {
        // Re-roll if not 6
        boolean[] ret = new boolean[] {false, false, false, false, false, false};
        
        for (int die = 0; die < dice.length; die++){
            if (dice[die] != 6){
                ret[die] = true;
            }
        }
        return ret;
    }

    public boolean leaveTokyo(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames) {
        if (super.getHealth() == 1) {
            return true;
        }
        return false;
    }
}