public class Main  implements Runnable{

    GUI gui = new GUI();

    public static void main(String[] args) {

            // creating a thread for run method
        new Thread(new Main()).start();
    }

    public void run() {
        while (true) {
            gui.repaint();
            if (!gui.reseter) {
                gui.checkVictoryStatus();
            }
        }
    }
}
