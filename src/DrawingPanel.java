import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawingPanel extends JPanel {
    ArrayList<Drawable> drawables = new ArrayList<>();

    public void addObject(Drawable d) {
        drawables.add(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Drawable d : drawables) {
            d.draw(g);
        }
    }
}
