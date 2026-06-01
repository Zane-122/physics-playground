import java.awt.Color;
import java.awt.Graphics;

public class Particle implements Drawable {
    private Util.Point position;
    private float lifespan;
    private Color color;

    public Particle(Util.Point p, Color c) {
        position = p;
        lifespan = Constants.particleLifespan;
        color = c;
    }

    public boolean isDead() {
        return lifespan <= 0;
    }

    public void fade() {
        lifespan -= 1.0f;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) lifespan));
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