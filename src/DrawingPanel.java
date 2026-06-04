import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {
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
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            return;
        }

        if (!polygonSpawnActive) {
            setCursor(Cursor.getDefaultCursor());
        }
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

        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }
}
