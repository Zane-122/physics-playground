import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private List<PhysicsObject> objects = new ArrayList<>();
    private double gravity = Constants.gravity;

    public void add(PhysicsObject obj) { objects.add(obj); }
    
    public void remove(PhysicsObject obj) { objects.remove(obj); }
    
    public List<PhysicsObject> getObjects() { return Collections.unmodifiableList(objects); }

    public double getGravity() { return gravity; }

    public void setGravity(double gravity) { this.gravity = gravity; }

    public double getGravityStep() { return gravity * Constants.timeStep; }

    public void updatePhysicsObjects() {
        for (PhysicsObject obj : objects) {
            obj.update(this);
        }
    }
}
