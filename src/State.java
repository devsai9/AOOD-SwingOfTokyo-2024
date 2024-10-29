public class State {
    private int currentTurn;
    private int currentPlayer;
    private int inTokyo;
    private int[] dice;
    private int[] playerHealths;
    private int[] playerFames;

    // Getters
    public int getCurrentTurn(){
        return currentTurn;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getInTokyo() {
        return inTokyo;
    }

    public int[] getDice() {
        return dice.clone();
    }

    public int[] getPlayerHealths() {
        return playerHealths.clone();
    }

    public int[] getPlayerFames() {
        return playerFames.clone();
    }


    // Setters
    public void setCurrentTurn(int currentTurn){
        this.currentTurn = currentTurn;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setInTokyo(int inTokyo) {
        this.inTokyo = inTokyo;
    }

    public void setDice(int[] dice) {
        this.dice = dice;
    }

    public void setPlayerHealths(int[] playerHealths) {
        this.playerHealths = playerHealths;
    }

    public void setPlayerFames(int[] playerFames) {
        this.playerFames = playerFames;
    }
}