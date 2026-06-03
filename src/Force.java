public abstract class Force {
    public Util.Point position;
    public double strength;
    
    public Force(Util.Point position, double strength) {
        this.position = position;
        this.strength = strength;
    }

    public final void update(Simulation sim) {
        for (PhysicsObject object : sim.getObjects()) {
            apply(object);
        }
    }

    public abstract void apply(PhysicsObject object);
}
