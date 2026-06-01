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
    private double spawnedPolygonRadius = 40;
    private static final double spawnedPolygonMass = 1;

    public DrawingPanel(Simulation sim) {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (polygonSpawnActive) {
                    spawnPolygon(new Util.Point(e.getX(), e.getY()));
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

    private void spawnPolygon(Util.Point position) {
        Polygon polygon = new Polygon(spawnPolygonSides, spawnedPolygonRadius, position);
        PhysicsObject object = new PhysicsObject(polygon, position, spawnedPolygonMass);
        object.addComponent(new Gravity());
        object.addComponent(new Collision());
        sim.add(object);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Drawable d : drawables) {
            d.draw(g);
        }

        for (PhysicsObject o : sim.getObjects()) {
            o.draw(g);
        }
    }
}
