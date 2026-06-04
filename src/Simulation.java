import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private List<PhysicsObject> objects = new ArrayList<>();
    private List<Force> forces = new ArrayList<>();
    private double gravity = Constants.gravity;
    private double restitution = Constants.restitution;
    private double friction = Constants.contactFrictionRetention;

    public void add(PhysicsObject obj) { objects.add(obj); }
    public void addForce(Force force) { forces.add(force); }
    public void removeForce(Force force) { forces.remove(force); }
    
    public void remove(PhysicsObject obj) { objects.remove(obj); }
    
    public List<PhysicsObject> getObjects() { return Collections.unmodifiableList(objects); }

    public double getGravity() { return gravity; }

    public void setGravity(double gravity) { this.gravity = gravity; }

    public void setBounciness(double restitution) { this.restitution = restitution; }

    public double getBounciness() { return restitution; }

    public void setFriction(double friction) { this.friction = friction; }

    public double getFriction() { return friction; }

    public double getGravityStep() { return gravity * Constants.timeStep; }

    public void update() {

        for (PhysicsObject obj : objects) {
            obj.update(this);
        }

        for (int i = forces.size() - 1; i >= 0; i--) {
            Force force = forces.get(i);
            force.update(this);
            if (force.isExpired()) {
                forces.remove(i);
            }
        }

        for (int i = 0; i < Constants.collisionIterations; i++) {
            for (PhysicsObject obj : objects) {
                obj.resolveCollisions(this);
            }
        }
    }
}
