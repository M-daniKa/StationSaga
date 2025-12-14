package game;
import javax.swing.SwingUtilities;
import ui.StartingPage;

public class stationSagaGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartingPage startingPage = new StartingPage();
            startingPage.setVisible(true);
        });
    }
}

