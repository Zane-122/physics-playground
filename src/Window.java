import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window {
    private double width;
    private double height;

    public Window(double w, double h) {
        width = w;
        height = h;
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize((int) width, (int) height);
            frame.setVisible(true);
        });
    }
}


