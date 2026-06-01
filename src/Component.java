public abstract class Component {
    public void initialize(PhysicsObject object) {
        // do nothing by default
    }

    public abstract void update(PhysicsObject object, Simulation sim);
}