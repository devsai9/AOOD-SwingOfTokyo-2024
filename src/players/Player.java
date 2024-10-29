package players;

public abstract class Player {
    private int health = 10;
    private int fame = 0;
    private boolean inTokyo = false;
    private int id;

    public void setHealth(int health) {
        this.health = health;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public int getHealth() {return health;}

    public abstract boolean[] rerollDice(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames);

    public abstract boolean leaveTokyo(int currentTurn, int currentPlayer, int inTokyo, int[] dice, int[] playerHealths, int[] playerFames);

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
}