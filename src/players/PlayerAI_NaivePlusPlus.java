package players;
import java.util.*;

public class PlayerAI_NaivePlusPlus extends Player {

    @Override
    public boolean[] rerollDice(
            int currentTurn,
            int currentPlayer,
            int inTokyo,
            int[] dice,
            int[] playerHealths,
            int[] playerFames
    ) {
        
        /*  Six possible combinations:
            Health only
            Damage only
            Fame only
            Health + Damage
            Health + Fame
            Damage + Fame
        */
        int playersAlive = alive(playerHealths);
        // If there are two players or a player is close to winning
        if(
                playersAlive == 2
                        || (inTokyo >= 0 && playerFames[inTokyo] > 9)
        ) return stratAggressive(dice);
        else { return stratPassive(dice, currentPlayer, inTokyo,
                playerHealths); }
    }

    @Override
    public boolean leaveTokyo(
            int currentTurn,
            int currentPlayer,
            int inTokyo,
            int[] dice,
            int[] playerHealths,
            int[] playerFames
    ) {
        // If there are too many players or not close to winning
        int playersAlive = alive(playerHealths);
        int maxFame = maxFame(playerFames, currentPlayer);
        if (playersAlive == 2 && maxFame > 13) {
            return false;
        }
        if (playerHealths[currentPlayer] < 7
                || playerFames[currentPlayer] < 11
                || playersAlive > 2) {
            return true;
        }
        return false;
    }



    private int alive(int[] healths) {
        return Arrays.stream(healths)
                .map(x -> x > 0 ? 1 : 0)
                .sum();
    }

    private int maxFame(int fames[], int currentPlayer) {
        int max = 0;
        for (int i = 0; i < fames.length; i++) {
            if (fames[i] > max && i != currentPlayer) {
                max = fames[i];
            }
        }
        return max;
    }

    private boolean[] stratAggressive(int[] dice) {
        boolean[] rerolls = new boolean[6];
        int numFours = 0;
        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == 6) {
                rerolls[i] = false;
            }
            else if (dice[i] == 4) {
                numFours++;
                rerolls[i] = false;
            }
            else {
                rerolls[i] = true;
            }
        }
        rerolls = checkFours(dice, rerolls, numFours);
        return rerolls;
    }

    private boolean[] stratPassive(int[] dice, int currentPlayer,
                                   int inTokyo, int[] playerHealths) {
        boolean[] rerolls = new boolean[6];
        int myHealth = playerHealths[currentPlayer];
        int healingNeeded = 12 - myHealth;
        int numFours = 0;

        for (int i = 0; i < dice.length; i++) {
            // If the dice shows 5 and more healing can be obtained
            if (dice[i] == 5 && healingNeeded > 0 && currentPlayer != inTokyo) {
                healingNeeded--;
                rerolls[i] = false;
            }
            else if (dice[i] == 4) {
                numFours++;
                rerolls[i] = false;
            }
            // Otherwise..
            else {
                rerolls[i] = true;
            }
        }
        rerolls = checkFours(dice, rerolls, numFours);
        return rerolls;
    }

    private boolean[] checkFours(int[] dice, boolean[] rerolls, int numFours) {
        // If there are more than three fours
        if (numFours > 3) {
            int extraFours = numFours - 3;
            for (int i = 0; i < rerolls.length; i++) {
                if (dice[i] == 4 && extraFours > 0) {
                    rerolls[i] = true;
                    extraFours--;
                }
                else if (extraFours == 0) {
                    break;
                }
            }
        }
        // If there is only one four
        else if (numFours == 1) {
            for (int i = 0; i < rerolls.length; i++) {
                if (dice[i] == 4) {
                    rerolls[i] = true;
                    break;
                }
            }
        }

        return rerolls;
    }

}