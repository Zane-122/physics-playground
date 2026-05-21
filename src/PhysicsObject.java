import java.awt.Graphics;

public class PhysicsObject implements Drawable {
    private Util.Point position;
    private double velocityX;
    private double velocityY;
    private Drawable visual;

    private double mass;

    public PhysicsObject(Drawable d, Util.Point p, double m) {
        visual = d;
        position = p.toPixels();
        velocityX = 0;
        velocityY = 0;
        mass = m;
    }

    public void update() {
        velocityX += 0;
        velocityY += Constants.gravityStep;

        position = new Util.Point(position.x() + velocityX, position.y() + velocityY);
        visual.setPosition(position);
    }

    @Override
    public final void draw(Graphics g) {
        visual.draw(g);
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
