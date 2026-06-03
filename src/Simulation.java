import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private List<PhysicsObject> objects = new ArrayList<>();
    private List<Force> forces = new ArrayList<>();
    private double gravity = Constants.gravity;

    public void add(PhysicsObject obj) { objects.add(obj); }
    public void addForce(Force force) { forces.add(force); }
    
    public void remove(PhysicsObject obj) { objects.remove(obj); }
    
    public List<PhysicsObject> getObjects() { return Collections.unmodifiableList(objects); }

    public double getGravity() { return gravity; }

    public void setGravity(double gravity) { this.gravity = gravity; }

    public double getGravityStep() { return gravity * Constants.timeStep; }

    public void update(ParticleSystem particleSystem) {
        particleSystem.spawnAndUpdateParticles(this);

        for (PhysicsObject obj : objects) {
            obj.update(this);
        }

        for (Force force : forces) {
            force.update(this);
        }

        for (int i = 0; i < Constants.collisionIterations; i++) {
            for (PhysicsObject obj : objects) {
                obj.resolveCollisions(this);
            }
            particleSystem.resolveParticleCollisions(this);
        }
    }
}
