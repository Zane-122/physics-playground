import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Window {
    private double width;
    private double height;
    private String title;
    private DrawingPanel panel;

    public Window(double w, double h, String t, DrawingPanel panel) {
        width = w;
        height = h;
        title = t;
        this.panel = panel;
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize((int) width, (int) height);
            frame.setVisible(true);
            frame.add(panel);
        });
    }
}

