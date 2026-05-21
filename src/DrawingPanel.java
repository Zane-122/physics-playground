import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {
    private ArrayList<Drawable> drawables = new ArrayList<>();
    private Util.Point mousePosition = null;
    private boolean mouseHeld = false;

    public DrawingPanel() {
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
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (mouseHeld) {
                    mousePosition = new Util.Point(e.getX(), e.getY());
                }
            }
         };

        addMouseListener(ma);
        addMouseMotionListener(ma);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Drawable d : drawables) {
            d.draw(g);
        }
    }
}
