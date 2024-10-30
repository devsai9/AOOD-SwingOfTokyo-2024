public class Main {
    public static void main(String[] args) {
        // GameEngine ge = new GameEngine();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                KOT_Swing k = new KOT_Swing();
            }
        });
    }
}