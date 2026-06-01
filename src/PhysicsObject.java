import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PhysicsObject implements Drawable {
    private Util.Point position;
    private double velocityX;
    private double velocityY;
    private Drawable visual;

    private double mass;
    private PolygonHitbox hitbox;

    private ArrayList<Component> components = new ArrayList<>();

    /**
     * Creates a new PhysicsObject with the given visual representation, position, and mass.
     * @param visual the visual representation of the object
     * @param p the initial position of the object
     * @param m the mass of the object
     */
    public PhysicsObject(Drawable visual, PolygonHitbox hitbox, Util.Point p, double m) {
        this.visual = visual;
        this.hitbox = hitbox;
        position = p;
        velocityX = 0;
        velocityY = 0;
        mass = m;
    }

    /**
     * Creates a new PhysicsObject with the given visual representation, position, mass, and initial velocity.
     * @param visual the visual representation of the object
     * @param p the initial position of the object
     * @param m the mass of the object
     * @param initialVelocityX the initial velocity in the x-direction
     * @param initialVelocityY the initial velocity in the y-direction
     */
    public PhysicsObject(Drawable visual, PolygonHitbox hitbox, Util.Point p, double m, double initialVelocityX, double initialVelocityY) {
        this.visual = visual;
        this.hitbox = hitbox;
        position = p;
        velocityX = initialVelocityX;
        velocityY = initialVelocityY;
        mass = m;
    }

    /**
     * Creates a new PhysicsObject with the Polygon as the hitbox
     * @param visual
     * @param p
     * @param m
     */
    public PhysicsObject(Polygon polygon, Util.Point p, double m) {
        this.visual = polygon;
        this.hitbox = new PolygonHitbox(polygon);
        position = p;
        velocityX = 0;
        velocityY = 0;
        mass = m;
    }

    /**
     * Creates a new PhysicsObject with the Polygon as the hitbox
     * @param polygon Specifically a polygon that will also be the hitbox 
     * @param p the initial position of the object
     * @param m the mass of the object
     * @param initialVelocityX the initial velocity in the x-direction
     * @param initialVelocityY thre initial velocity in the y-direction
     */
    public PhysicsObject(Polygon polygon, Util.Point p, double m, double initialVelocityX, double initialVelocityY) {
        this.visual = polygon;
        this.hitbox = new PolygonHitbox(polygon);
        position = p;
        velocityX = initialVelocityX;
        velocityY = initialVelocityY;
        mass = m;
    }

    public void update(Simulation sim) {
        for (Component c : components) {
            c.update(this, sim);
        }

        setPosition(new Util.Point(position.x() + velocityX, position.y() + velocityY));
        hitbox.setPosition(position);
        visual.setPosition(position);
    }

    public void addComponent(Component c) {
        components.add(c);
        c.initialize(this);
    }

    public Drawable getVisual() {
        return visual;
    }

    @Override
    public final void draw(Graphics g) {
        g.setColor(Color.RED);
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

    public void setVelocity(double x, double y) {
        velocityX = x;
        velocityY = y;
    }

    public void setVelocityX(double value) {
        velocityX = value;
    }

    public void setVelocityY(double value) {
        velocityY = value;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public boolean isColliding(PhysicsObject po) {
        return hitbox.isColliding(po.hitbox);
    }
}
