import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class PhysicsObject implements Drawable {
    private Util.Point position;
    private double velocityX;
    private double velocityY;
    private double angle;
    private double angularVelocity;
    private double momentOfInertia;
    private Drawable visual;

    private double mass;
    private PolygonHitbox hitbox;
    private boolean particle;
    private boolean staticBody;

    private Color color = Color.RED;

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
        initMomentOfInertia();
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
        initMomentOfInertia();
    }

    /**
     * Creates a new PhysicsObject with the Polygon as the hitbox
     * @param visual
     * @param p
     * @param m
     */
    public PhysicsObject(Polygon polygon, Util.Point p, double m, Color color) {
        this.visual = polygon;
        this.hitbox = new PolygonHitbox(polygon);
        position = p;
        velocityX = 0;
        velocityY = 0;
        this.color = color;
        mass = m;
        initMomentOfInertia();
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
        initMomentOfInertia();
    }

    private void initMomentOfInertia() {
        if (mass <= 0) {
            momentOfInertia = 1;
            return;
        }
        double radius = hitbox.getRadius();
        momentOfInertia = 0.5 * mass * radius * radius;
    }

    public void update(Simulation sim) {
        for (Component c : components) {
            if (c instanceof Collision) continue;
            c.update(this, sim);
        }

        if (!isStatic()) {
            velocityX *= 0.999;
            velocityY *= 0.999;
            angularVelocity *= 0.995;

            angle += angularVelocity;
            setPosition(new Util.Point(position.x() + velocityX, position.y() + velocityY));
            syncTransform();
        }
    }

    public void resolveCollisions(Simulation sim) {
        for (Component c : components) {
            if (c instanceof Collision) {
                c.update(this, sim);
            }
        }
    }

    public void syncTransform() {
        hitbox.setPosition(position);
        hitbox.setRotation(angle);
        visual.setPosition(position);
        if (visual instanceof Polygon polygon) {
            polygon.setRotation(angle);
        }
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
        Color oldColor = g.getColor();
        g.setColor(color);
        visual.draw(g);
        g.setColor(oldColor);
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        syncTransform();
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getMomentOfInertia() {
        return momentOfInertia;
    }

    public double getMass() {
        return mass;
    }

    public boolean isStatic() {
        return staticBody || mass == 0;
    }

    public void setStatic(boolean staticBody) {
        this.staticBody = staticBody;
    }

    public boolean isParticle() {
        return particle;
    }

    public void setParticle(boolean particle) {
        this.particle = particle;
    }

    public PolygonHitbox getHitbox() {
        return hitbox;
    }

    public boolean isColliding(PhysicsObject po) {
        return hitbox.isColliding(po.hitbox);
    }

    public void setRotation(double rotation) {
        angle = rotation;
        syncTransform();
    }
}
