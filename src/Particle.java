import java.awt.Color;
import java.awt.Graphics;

public class Particle implements Drawable {
    private Util.Point position;
    private double lifespan;
    private final double initialLifespan;
    private Color color;

    public Particle (Util.Point p, Color c, double lifespan) {
        position = p;
        this.lifespan = lifespan;
        this.initialLifespan = lifespan;
        color = c;
    }

    public boolean isDead() {
        return lifespan <= 0;
    }

    public void fade() {
        lifespan -= 1.0f;
    }

    private int currentAlpha() {
        if (initialLifespan <= 0) {
            return 0;
        }
        double lifeRatio = Math.max(0.0, Math.min(1.0, lifespan / initialLifespan));
        return (int) Math.round(255 * (1 - Math.pow(1 - lifeRatio, 3)));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), currentAlpha()));
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