import java.awt.Graphics;

public class PhysicsObject implements Drawable {
    private Util.Point position;
    private double velocityX;
    private double velocityY;
    private Drawable visual;

    private double mass;

    /**
     * Creates a new PhysicsObject with the given visual representation, position, and mass.
     * @param d the visual representation of the object
     * @param p the initial position of the object
     * @param m the mass of the object
     */
    public PhysicsObject(Drawable d, Util.Point p, double m) {
        visual = d;
        position = p;
        velocityX = 0;
        velocityY = 0;
        mass = m;
    }

    /**
     * Creates a new PhysicsObject with the given visual representation, position, mass, and initial velocity.
     * @param d the visual representation of the object
     * @param p the initial position of the object
     * @param m the mass of the object
     * @param initialVelocityX the initial velocity in the x-direction
     * @param initialVelocityY the initial velocity in the y-direction
     */
    public PhysicsObject(Drawable d, Util.Point p, double m, double initialVelocityX, double initialVelocityY) {
        visual = d;
        position = p;
        velocityX = initialVelocityX;
        velocityY = initialVelocityY;
        mass = m;
    }

    public void update() {
        velocityX += 0;
        velocityY += Constants.gravityStep;

        position = new Util.Point(position.x() + velocityX, position.y() + velocityY);
        visual.setPosition(position);
    }

    public Drawable getVisual() {
        return visual;
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
