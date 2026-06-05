import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {
    public enum MouseForceMode { NONE, PUSH, PULL }

    private ArrayList<Drawable> drawables = new ArrayList<>();
    private Util.Point mousePosition = null;
    private boolean mouseHeld = false;
    private boolean mouseClicked = false;
    private Util.Point clickPosition = null;
    private Simulation sim;
    private int spawnPolygonSides = 5;
    private boolean polygonSpawnActive = false;
    private double spawnedPolygonRadius = 30;
    private boolean explosionSpawnActive =false;
    private double explosionStrength = 2000;
    private Util.Point pendingExplosionPosition = null;
    private MouseForceMode mouseForceMode = MouseForceMode.NONE;
    private double mouseForceStrength = 4000;
    private static final int defaultPolygonSides = 5;
    private static final double defaultPolygonRadius = 30;
    private static final double minimumSpawnedPolygonMass = 0.25;

    public DrawingPanel(Simulation sim) {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (polygonSpawnActive) {
                    spawnPolygon(new Util.Point(e.getX(), e.getY()));
                    mouseHeld = false;
                    return;
                }

                if (explosionSpawnActive) {
                    pendingExplosionPosition = new Util.Point(e.getX(), e.getY());
                    mouseHeld = false;
                    return;
                }

                mouseHeld = true;
                mousePosition = new Util.Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                mouseHeld = false;
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mouseClicked = true;
                clickPosition = new Util.Point(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (mouseHeld) {
                    mousePosition = new Util.Point(e.getX(), e.getY());
                }
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);

        this.sim = sim;
    }

    public Util.Point getMousePos() {
        return mousePosition;
    }

    public boolean isMouseHeld() {
        return mouseHeld;
    }

    public void addObject(Drawable d) {
        drawables.add(d);
    }

    public void removeObject(Drawable d) {
        drawables.remove(d);
    }

    public boolean wasClicked() {
        boolean clicked = mouseClicked;
        mouseClicked = false;
        return clicked;
    }

    public Util.Point getClickPosition() {
        return clickPosition;
    }

    public Simulation getSimulation() {
        return sim;
    }

    public void startPolygonSpawn(int sides) {
        spawnPolygonSides = sides;
        polygonSpawnActive = true;
        explosionSpawnActive = false;
        mouseForceMode = MouseForceMode.NONE;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void stopPolygonSpawn() {
        polygonSpawnActive = false;
        setCursor(Cursor.getDefaultCursor());
    }

    public int getSpawnPolygonSides() {
        return spawnPolygonSides;
    }

    public void setSpawnPolygonSides(int sides) {
        spawnPolygonSides = sides;
    }

    public void setSpawnedPolygonRadius(double radius) {
        spawnedPolygonRadius = radius;
    }

    public void setExplosionSpawnActive(boolean active) {
        explosionSpawnActive = active;
        if (active) {
            polygonSpawnActive = false;
            mouseForceMode = MouseForceMode.NONE;
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            return;
        }

        if (!polygonSpawnActive && mouseForceMode == MouseForceMode.NONE) {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void setMouseForceMode(MouseForceMode mode) {
        mouseForceMode = mode == null ? MouseForceMode.NONE : mode;
        if (mouseForceMode != MouseForceMode.NONE) {
            polygonSpawnActive = false;
            explosionSpawnActive = false;
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return;
        }

        if (!polygonSpawnActive && !explosionSpawnActive) {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public MouseForceMode getMouseForceMode() {
        return mouseForceMode;
    }

    public void setMouseForceStrength(double strength) {
        mouseForceStrength = strength;
    }

    public double getMouseForceStrength() {
        return mouseForceStrength;
    }

    public void setExplosionStrength(double strength) {
        explosionStrength = strength;
    }

    public double getExplosionStrength() {
        return explosionStrength;
    }

    public Util.Point consumeExplosionSpawnPosition() {
        Util.Point position = pendingExplosionPosition;
        pendingExplosionPosition = null;
        return position;
    }

    private void spawnPolygon(Util.Point position) {
        Polygon polygon = new Polygon(spawnPolygonSides, spawnedPolygonRadius, position);
        PhysicsObject object = new PhysicsObject(polygon, position, calculateSpawnedPolygonMass(), Color.CYAN);
        object.addComponent(new Gravity());
        object.addComponent(new Collision());
        sim.add(object);
        repaint();
    }

    private double calculateSpawnedPolygonMass() {
        double defaultArea = calculateRegularPolygonArea(defaultPolygonSides, defaultPolygonRadius);
        double spawnedArea = calculateRegularPolygonArea(spawnPolygonSides, spawnedPolygonRadius);
        return Math.max(minimumSpawnedPolygonMass, spawnedArea / defaultArea);
    }

    private double calculateRegularPolygonArea(int sides, double radius) {
        return 0.5 * sides * radius * radius * Math.sin(2 * Math.PI / sides);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        Composite oldComposite = g2d.getComposite();
        Color oldColor = g2d.getColor();

        // Glow pass: render a faint wide outline using each object's own color.
        g2d.setStroke(new BasicStroke(10));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (Drawable d : drawables) {
            d.draw(g2d);
        }
        for (PhysicsObject o : sim.getObjects()) {
            o.draw(g2d);
        }

        // Main pass: draw objects normally on top.
        g2d.setStroke(new BasicStroke(3));
        g2d.setComposite(oldComposite);
        g2d.setColor(oldColor);
        for (Drawable d : drawables) {
            d.draw(g2d);
        }
        for (PhysicsObject o : sim.getObjects()) {
            o.draw(g2d);
        }

        drawMouseForceOverlay(g2d);

        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
        g2d.setComposite(oldComposite);
    }

    private void drawMouseForceOverlay(Graphics2D g2d) {
        if (!mouseHeld || mousePosition == null || mouseForceMode == MouseForceMode.NONE) return;

        int x = (int) mousePosition.x();
        int y = (int) mousePosition.y();
        int radius = (int) Math.max(36, Math.min(96, 28 + Math.sqrt(mouseForceStrength) * 0.45));
        double phase = (System.currentTimeMillis() % 1200) / 1200.0;
        double animatedRadius = mouseForceMode == MouseForceMode.PUSH
            ? radius * (0.45 + phase * 0.50)
            : radius * (0.95 - phase * 0.50);
        Color forceColor = mouseForceMode == MouseForceMode.PUSH
            ? new Color(184, 118, 88)
            : new Color(116, 145, 137);

        Stroke oldStroke = g2d.getStroke();
        Composite oldComposite = g2d.getComposite();
        Color oldColor = g2d.getColor();

        g2d.setColor(forceColor);
        g2d.setStroke(new BasicStroke(2));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.16f));
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);

        int animatedDiameter = (int) Math.round(animatedRadius * 2);
        int animatedX = (int) Math.round(x - animatedRadius);
        int animatedY = (int) Math.round(y - animatedRadius);
        float animatedAlpha = (float) (0.22 * (1.0 - Math.abs(phase - 0.5) * 0.8));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animatedAlpha));
        g2d.drawOval(animatedX, animatedY, animatedDiameter, animatedDiameter);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.28f));
        g2d.fillOval(x - 3, y - 3, 6, 6);

        g2d.setStroke(oldStroke);
        g2d.setComposite(oldComposite);
        g2d.setColor(oldColor);
    }
}
