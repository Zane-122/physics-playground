import java.awt.Graphics;
import java.awt.Color;

public class Particle implements Drawable {
    private Util.Point position;
    private float lifespan;

    public Particle(Util.Point p) {
        position = p;
        lifespan = 255.0f;
    }

    public boolean isDead() {
        return lifespan <= 0;
    }

    public void fade() {
        lifespan -= 1.0f;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(255, 255, 255, (int) lifespan));
        g.fillOval((int) position.x() - 5, (int) position.y(), 8, 8);
    }

    @Override
    public void setPosition(Util.Point p) {
        position = p;
    }

    @Override
    public Util.Point getPosition() {
        return position;
    }
}