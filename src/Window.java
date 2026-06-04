import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private static final Color PANEL_BACKGROUND = new Color(0x1c1c1c);
    private static final Color BUTTON_BACKGROUND = new Color(72, 105, 178);
    private static final Color SPAWN_BACKGROUND = new Color(117, 83, 160);
    private static final Color SPAWN_ACTIVE_BACKGROUND = new Color(45, 139, 99);
    private static final Color TEXT_LIGHT = new Color(245, 247, 250);
    private static final Color TEXT_DARK = new Color(33, 37, 45);
    private static final Color INPUT_BORDER = new Color(198, 205, 217);
    private static final Color INPUT_ERROR_BORDER = new Color(196, 62, 62);
    private static final Color INPUT_PENDING_BORDER = new Color(204, 140, 38);
    private static final Color INPUT_SET_BORDER = SPAWN_ACTIVE_BACKGROUND;
    private static final Border INPUT_DEFAULT_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_BORDER),
        new EmptyBorder(7, 9, 7, 9)
    );
    private static final Border INPUT_ERROR_STYLE = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_ERROR_BORDER),
        new EmptyBorder(7, 9, 7, 9)
    );
    private static final Border INPUT_PENDING_STYLE = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_PENDING_BORDER),
        new EmptyBorder(7, 9, 7, 9)
    );
    private static final Border INPUT_SET_STYLE = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(INPUT_SET_BORDER),
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

            JPanel toolbar = createToolbar();
            frame.add(toolbar, BorderLayout.NORTH);
            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private JPanel createToolbar() {
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));
        toolbarPanel.setOpaque(true);
        toolbarPanel.setBackground(TOOLBAR_BACKGROUND);
        toolbarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 230, 238)),
            new EmptyBorder(4, 0, 4, 0)
        ));

        JToolBar settingsToolbar = createToolbarRow();
        JToolBar spawnToolbar = createToolbarRow();

        JToggleButton spawnButton = new JToggleButton("Spawn Objects");
        styleButton(spawnButton, SPAWN_BACKGROUND);
        spawnButton.setToolTipText("Toggle object spawning on or off");

        JToggleButton explosionButton = new JToggleButton("Spawn Explosion");
        styleButton(explosionButton, SPAWN_BACKGROUND);
        explosionButton.setToolTipText("Toggle explosion spawning on or off");

        JTextField gravityField = createNumberField(Double.toString(panel.getSimulation().getGravity()), 72);
        gravityField.setToolTipText("Gravity value");
        JButton gravityButton = new JButton("Gravity Set");
        styleButton(gravityButton, SPAWN_ACTIVE_BACKGROUND);
        gravityButton.setToolTipText("Apply the gravity number beside this button");
        markApplied(gravityField);
        gravityButton.addActionListener(e -> {
            turnOffSpawn(spawnButton);
            applyGravity(gravityField, gravityButton);
        });
        gravityField.addActionListener(e -> {
            turnOffSpawn(spawnButton);
            applyGravity(gravityField, gravityButton);
        });
        addSettingChangeListener(
            gravityField,
            spawnButton,
            () -> markPending(gravityField),
            () -> updateSettingButton(gravityButton, "Gravity Set", "Set Gravity", false)
        );

        JTextField sidesField = createNumberField(Integer.toString(panel.getSpawnPolygonSides()), 54);
        sidesField.setToolTipText("Polygon sides");
        JButton sidesButton = new JButton("Sides Set");
        styleButton(sidesButton, SPAWN_ACTIVE_BACKGROUND);
        sidesButton.setToolTipText("Apply the polygon side count beside this button");
        markApplied(sidesField);
        sidesButton.addActionListener(e -> {
            turnOffSpawn(spawnButton);
            updateSpawnSides(sidesField, sidesButton);
        });
        sidesField.addActionListener(e -> {
            turnOffSpawn(spawnButton);
            updateSpawnSides(sidesField, sidesButton);
        });
        addSettingChangeListener(
            sidesField,
            spawnButton,
            () -> markPending(sidesField),
            () -> updateSettingButton(sidesButton, "Sides Set", "Set Sides", false)
        );

        spawnButton.addActionListener(e -> handleSpawnToggle(spawnButton, sidesField, sidesButton, explosionButton));

        JLabel sizeLabel = createToolbarLabel("Size Set: 40");
        sizeLabel.setForeground(SPAWN_ACTIVE_BACKGROUND);
        JSlider sizeSlider = new JSlider(10, 100, 40);
        sizeSlider.setPreferredSize(new Dimension(150, 32));
        sizeSlider.setMaximumSize(new Dimension(150, 32));
        sizeSlider.setOpaque(false);
        sizeSlider.setFocusable(false);
        sizeSlider.setToolTipText("Shape size");
        sizeSlider.addChangeListener(e -> {
            int size = sizeSlider.getValue();
            turnOffSpawn(spawnButton);
            panel.setSpawnedPolygonRadius(size);
            sizeLabel.setText("Size Set: " + size);
        });

        JLabel explosionLabel = createToolbarLabel("Push 2000");
        JSlider explosionSlider = new JSlider(500, 8000, 2000);
        explosionSlider.setPreferredSize(new Dimension(150, 32));
        explosionSlider.setMaximumSize(new Dimension(150, 32));
        explosionSlider.setOpaque(false);
        explosionSlider.setFocusable(false);
        explosionSlider.setToolTipText("Explosion push strength");
        explosionSlider.addChangeListener(e -> {
            int strength = explosionSlider.getValue();
            turnOffSpawn(spawnButton);
            panel.setExplosionStrength(strength);
            explosionLabel.setText("Push " + strength);
        });

        explosionButton.addActionListener(e -> handleExplosionToggle(explosionButton, spawnButton));

        settingsToolbar.add(gravityButton);
        settingsToolbar.add(Box.createHorizontalStrut(6));
        settingsToolbar.add(gravityField);
        settingsToolbar.add(Box.createHorizontalStrut(12));
        settingsToolbar.add(sidesButton);
        settingsToolbar.add(Box.createHorizontalStrut(6));
        settingsToolbar.add(sidesField);
        settingsToolbar.add(Box.createHorizontalStrut(16));
        settingsToolbar.add(sizeLabel);
        settingsToolbar.add(Box.createHorizontalStrut(6));
        settingsToolbar.add(sizeSlider);

        spawnToolbar.add(spawnButton);
        spawnToolbar.add(Box.createHorizontalStrut(16));
        spawnToolbar.add(explosionButton);
        spawnToolbar.add(Box.createHorizontalStrut(6));
        spawnToolbar.add(explosionLabel);
        spawnToolbar.add(Box.createHorizontalStrut(6));
        spawnToolbar.add(explosionSlider);

        toolbarPanel.add(settingsToolbar);
        toolbarPanel.add(spawnToolbar);

        return toolbarPanel;
    }

    private JToolBar createToolbarRow() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOpaque(true);
        toolbar.setBackground(TOOLBAR_BACKGROUND);
        toolbar.setBorder(new EmptyBorder(4, 10, 4, 10));
        return toolbar;
    }

    private void applyGravity(JTextField gravityField, JButton gravityButton) {
        try {
            double gravity = Double.parseDouble(gravityField.getText().trim());
            if (!Double.isFinite(gravity)) {
                markInvalid(gravityField);
                updateSettingButton(gravityButton, "Gravity Set", "Set Gravity", false);
                return;
            }

            panel.getSimulation().setGravity(gravity);
            markApplied(gravityField);
            updateSettingButton(gravityButton, "Gravity Set", "Set Gravity", true);
        } catch (NumberFormatException ex) {
            markInvalid(gravityField);
            updateSettingButton(gravityButton, "Gravity Set", "Set Gravity", false);
        }
    }

    private void handleSpawnToggle(
        JToggleButton spawnButton,
        JTextField sidesField,
        JButton sidesButton,
        JToggleButton explosionButton
    ) {
        if (!spawnButton.isSelected()) {
            panel.stopPolygonSpawn();
            updateSpawnButton(spawnButton);
            return;
        }

        turnOffExplosion(explosionButton);

        if (!updateSpawnSides(sidesField, sidesButton)) {
            spawnButton.setSelected(false);
            updateSpawnButton(spawnButton);
            return;
        }

        panel.startPolygonSpawn(panel.getSpawnPolygonSides());
        updateSpawnButton(spawnButton);
    }

    private void handleExplosionToggle(JToggleButton explosionButton, JToggleButton spawnButton) {
        if (!explosionButton.isSelected()) {
            panel.setExplosionSpawnActive(false);
            updateExplosionButton(explosionButton);
            return;
        }

        turnOffSpawn(spawnButton);
        panel.setExplosionSpawnActive(true);
        updateExplosionButton(explosionButton);
    }

    private boolean updateSpawnSides(JTextField sidesField, JButton sidesButton) {
        try {
            int sides = Integer.parseInt(sidesField.getText().trim());
            if (sides < 3) {
                markInvalid(sidesField);
                updateSettingButton(sidesButton, "Sides Set", "Set Sides", false);
                return false;
            }

            panel.setSpawnPolygonSides(sides);
            markApplied(sidesField);
            updateSettingButton(sidesButton, "Sides Set", "Set Sides", true);
            return true;
        } catch (NumberFormatException ex) {
            markInvalid(sidesField);
            updateSettingButton(sidesButton, "Sides Set", "Set Sides", false);
            return false;
        }
    }

    private void updateSpawnButton(JToggleButton spawnButton) {
        if (spawnButton.isSelected()) {
            spawnButton.setText("Spawning");
            spawnButton.setBackground(SPAWN_ACTIVE_BACKGROUND);
            return;
        }

        spawnButton.setText("Spawn Objects");
        spawnButton.setBackground(SPAWN_BACKGROUND);
    }

    private void updateExplosionButton(JToggleButton explosionButton) {
        explosionButton.setText("Spawn Explosion");
        if (explosionButton.isSelected()) {
            explosionButton.setBackground(SPAWN_ACTIVE_BACKGROUND);
            return;
        }

        explosionButton.setBackground(SPAWN_BACKGROUND);
    }

    private void turnOffSpawn(JToggleButton spawnButton) {
        if (!spawnButton.isSelected()) return;

        spawnButton.setSelected(false);
        panel.stopPolygonSpawn();
        updateSpawnButton(spawnButton);
    }

    private void turnOffExplosion(JToggleButton explosionButton) {
        if (!explosionButton.isSelected()) return;

        explosionButton.setSelected(false);
        panel.setExplosionSpawnActive(false);
        updateExplosionButton(explosionButton);
    }

    private void addSettingChangeListener(
        JTextField field,
        JToggleButton spawnButton,
        Runnable markChanged,
        Runnable updateButton
    ) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                settingChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                settingChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                settingChanged();
            }

            private void settingChanged() {
                turnOffSpawn(spawnButton);
                markChanged.run();
                updateButton.run();
            }
        });
    }

    private void updateSettingButton(AbstractButton button, String setText, String pendingText, boolean applied) {
        if (applied) {
            button.setText(setText);
            button.setBackground(SPAWN_ACTIVE_BACKGROUND);
            return;
        }

        button.setText(pendingText);
        button.setBackground(BUTTON_BACKGROUND);
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

    private void markApplied(JTextField field) {
        field.setBorder(INPUT_SET_STYLE);
        field.setToolTipText("This value is applied.");
    }

    private void markPending(JTextField field) {
        field.setBorder(INPUT_PENDING_STYLE);
        field.setToolTipText("Press the set button to apply this value.");
    }

    private void markInvalid(JTextField field) {
        field.setBorder(INPUT_ERROR_STYLE);
        field.setToolTipText("Enter a valid number.");
        field.requestFocusInWindow();
        field.selectAll();
    }
}
