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

    public DrawingPanel(Simulation sim) {
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
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
        return mouseClicked;
    }

    public Util.Point getClickPosition() {
        return clickPosition;
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
