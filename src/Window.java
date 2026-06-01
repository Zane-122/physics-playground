import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

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
            frame.setLayout(new BorderLayout());

            JToolBar toolbar = new JToolBar();
            toolbar.setFloatable(false);

            JButton gravityButton = new JButton("Set Gravity");
            gravityButton.setToolTipText("Set the gravity value for the simulation");
            gravityButton.addActionListener(e -> promptForGravity(frame));

            JButton spawnButton = new JButton("Spawn Object");
            spawnButton.setToolTipText("Choose polygon sides, then click the canvas to spawn it");
            spawnButton.addActionListener(e -> promptForPolygonSides(frame));

            toolbar.add(gravityButton);
            toolbar.add(spawnButton);

            frame.add(toolbar, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private void promptForGravity(JFrame frame) {
        Simulation sim = panel.getSimulation();
        String value = JOptionPane.showInputDialog(
            frame,
            "Gravity",
            Double.toString(sim.getGravity())
        );

        if (value == null) return;

        try {
            double gravity = Double.parseDouble(value.trim());
            if (!Double.isFinite(gravity)) {
                JOptionPane.showMessageDialog(frame, "Please enter a finite number.");
                return;
            }

            sim.setGravity(gravity);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
        }
    }

    private void promptForPolygonSides(JFrame frame) {
        String value = JOptionPane.showInputDialog(frame, "Polygon sides", "5");

        if (value == null) return;

        try {
            int sides = Integer.parseInt(value.trim());
            if (sides < 3) {
                JOptionPane.showMessageDialog(frame, "Polygons need at least 3 sides.");
                return;
            }

            panel.startPolygonSpawn(sides);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a whole number.");
        }
    }
}
