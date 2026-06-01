import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {
    private ArrayList<Drawable> drawables = new ArrayList<>();
    private Util.Point mousePosition = null;
    private boolean mouseHeld = false;
    private Simulation sim;
    private Integer spawnPolygonSides = null;
    private static final double spawnedPolygonRadius = 40;
    private static final double spawnedPolygonMass = 1;

    public DrawingPanel(Simulation sim) {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (spawnPolygonSides != null) {
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

    public Simulation getSimulation() {
        return sim;
    }

    public void startPolygonSpawn(int sides) {
        spawnPolygonSides = sides;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    private void spawnPolygon(Util.Point position) {
        int sides = spawnPolygonSides;
        spawnPolygonSides = null;
        setCursor(Cursor.getDefaultCursor());

        Polygon polygon = new Polygon(sides, spawnedPolygonRadius, position);
        sim.add(new PhysicsObject(polygon, position, spawnedPolygonMass));
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
