import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation {
    private List<PhysicsObject> objects = new ArrayList<>();

    public void add(PhysicsObject obj) { 
        objects.add(obj);
    }
    
    public void remove(PhysicsObject obj) { objects.remove(obj); }
    public List<PhysicsObject> getObjects() { return Collections.unmodifiableList(objects); }

    public void updatePhysicsObjects() {
        for (PhysicsObject obj : objects) {
            obj.update(this);
        }
    }
}