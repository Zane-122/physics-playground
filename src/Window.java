import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Window {
    private static final Color TOOLBAR_BACKGROUND = Color.WHITE;
    private static final Color PANEL_BACKGROUND = new Color(245, 247, 250);
    private static final Color BUTTON_BACKGROUND = new Color(72, 105, 178);
    private static final Color SPAWN_BACKGROUND = new Color(117, 83, 160);
    private static final Color SPAWN_ACTIVE_BACKGROUND = new Color(45, 139, 99);
    private static final Color TEXT_LIGHT = new Color(245, 247, 250);
    private static final Color TEXT_DARK = new Color(33, 37, 45);
    private static final Color INPUT_BORDER = new Color(198, 205, 217);
    private static final Color INPUT_ERROR_BORDER = new Color(196, 62, 62);
    private static final Border INPUT_DEFAULT_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_BORDER),
        new EmptyBorder(7, 9, 7, 9)
    );
    private static final Border INPUT_ERROR_STYLE = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_ERROR_BORDER),
        new EmptyBorder(7, 9, 7, 9)
    );

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
            panel.setBackground(PANEL_BACKGROUND);

            JToolBar toolbar = createToolbar();
            frame.add(toolbar, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOpaque(true);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 230, 238)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        toolbar.setBackground(TOOLBAR_BACKGROUND);

        JTextField gravityField = createNumberField(Double.toString(panel.getSimulation().getGravity()), 72);
        gravityField.setToolTipText("Gravity value");
        JButton gravityButton = new JButton("Set Gravity");
        styleButton(gravityButton, BUTTON_BACKGROUND);
        gravityButton.setToolTipText("Apply the gravity number beside this button");
        gravityButton.addActionListener(e -> applyGravity(gravityField));
        gravityField.addActionListener(e -> applyGravity(gravityField));

        JTextField sidesField = createNumberField(Integer.toString(panel.getSpawnPolygonSides()), 54);
        sidesField.setToolTipText("Polygon sides");
        JToggleButton spawnButton = new JToggleButton("Spawn Object");
        styleButton(spawnButton, SPAWN_BACKGROUND);
        spawnButton.setToolTipText("Turn spawning on, then click the canvas to place polygons");
        spawnButton.addActionListener(e -> handleSpawnToggle(spawnButton, sidesField));
        sidesField.addActionListener(e -> updateSpawnSides(sidesField));
        sidesField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSpawnSidesQuietly(sidesField);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSpawnSidesQuietly(sidesField);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSpawnSidesQuietly(sidesField);
            }
        });

        JLabel sizeLabel = createToolbarLabel("Size 40");
        JSlider sizeSlider = new JSlider(10, 100, 40);
        sizeSlider.setPreferredSize(new Dimension(150, 32));
        sizeSlider.setMaximumSize(new Dimension(150, 32));
        sizeSlider.setOpaque(false);
        sizeSlider.setFocusable(false);
        sizeSlider.setToolTipText("Shape size");
        sizeSlider.addChangeListener(e -> {
            int size = sizeSlider.getValue();
            panel.setSpawnedPolygonRadius(size);
            sizeLabel.setText("Size " + size);
        });

        toolbar.add(gravityButton);
        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(gravityField);
        toolbar.add(Box.createHorizontalStrut(12));
        toolbar.add(spawnButton);
        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(sidesField);
        toolbar.add(Box.createHorizontalStrut(16));
        toolbar.add(sizeLabel);
        toolbar.add(Box.createHorizontalStrut(6));
        toolbar.add(sizeSlider);

        return toolbar;
    }

    private void applyGravity(JTextField gravityField) {
        try {
            double gravity = Double.parseDouble(gravityField.getText().trim());
            if (!Double.isFinite(gravity)) {
                markInvalid(gravityField);
                return;
            }

            panel.getSimulation().setGravity(gravity);
            markValid(gravityField);
        } catch (NumberFormatException ex) {
            markInvalid(gravityField);
        }
    }

    private void handleSpawnToggle(JToggleButton spawnButton, JTextField sidesField) {
        if (!spawnButton.isSelected()) {
            panel.stopPolygonSpawn();
            updateSpawnButton(spawnButton);
            return;
        }

        if (!updateSpawnSides(sidesField)) {
            spawnButton.setSelected(false);
            updateSpawnButton(spawnButton);
            return;
        }

        panel.startPolygonSpawn(panel.getSpawnPolygonSides());
        updateSpawnButton(spawnButton);
    }

    private boolean updateSpawnSides(JTextField sidesField) {
        try {
            int sides = Integer.parseInt(sidesField.getText().trim());
            if (sides < 3) {
                markInvalid(sidesField);
                return false;
            }

            panel.setSpawnPolygonSides(sides);
            markValid(sidesField);
            return true;
        } catch (NumberFormatException ex) {
            markInvalid(sidesField);
            return false;
        }
    }

    private void updateSpawnSidesQuietly(JTextField sidesField) {
        try {
            int sides = Integer.parseInt(sidesField.getText().trim());
            if (sides >= 3) {
                panel.setSpawnPolygonSides(sides);
                markValid(sidesField);
            }
        } catch (NumberFormatException ex) {
            // Wait for the user to finish editing before showing an error.
        }
    }

    private void updateSpawnButton(JToggleButton spawnButton) {
        if (spawnButton.isSelected()) {
            spawnButton.setText("Spawning");
            spawnButton.setBackground(SPAWN_ACTIVE_BACKGROUND);
            return;
        }

        spawnButton.setText("Spawn Object");
        spawnButton.setBackground(SPAWN_BACKGROUND);
    }

    private JTextField createNumberField(String value, int width) {
        JTextField field = new JTextField(value);
        field.setPreferredSize(new Dimension(width, 34));
        field.setMaximumSize(new Dimension(width, 34));
        field.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        field.setForeground(TEXT_DARK);
        field.setBackground(Color.WHITE);
        field.setBorder(INPUT_DEFAULT_BORDER);
        return field;
    }

    private JLabel createToolbarLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_DARK);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        return label;
    }

    private void styleButton(AbstractButton button, Color background) {
        button.setBackground(background);
        button.setForeground(TEXT_LIGHT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 70)),
            new EmptyBorder(8, 14, 8, 14)
        ));
    }

    private void markValid(JTextField field) {
        field.setBorder(INPUT_DEFAULT_BORDER);
        field.setToolTipText(null);
    }

    private void markInvalid(JTextField field) {
        field.setBorder(INPUT_ERROR_STYLE);
        field.setToolTipText("Enter a valid number.");
        field.requestFocusInWindow();
        field.selectAll();
    }
}
