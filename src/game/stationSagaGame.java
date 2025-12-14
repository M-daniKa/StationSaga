package game;
import javax.swing.SwingUtilities;
import ui.StartingPage;

public class stationSagaGame {
    public static void main(String[] args) {
        // Start the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            StartingPage startingPage = new StartingPage();
            startingPage.setVisible(true);
        });
    }
}

