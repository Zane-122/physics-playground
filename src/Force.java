public abstract class Force {
    public Util.Point position;
    public double strength;
    private double lifetime = Double.POSITIVE_INFINITY;
    private double age = 0;
    
    public Force(Util.Point position, double strength) {
        this.position = position;
        this.strength = strength;
    }

    public Force(Util.Point position, double strength, double lifetime) {
        this(position, strength);
        this.lifetime = lifetime;
    }

    public final void update(Simulation sim) {
        for (PhysicsObject object : sim.getObjects()) {
            apply(object);
        }

        age += Constants.timeStep;
    }

    public boolean isExpired() {
        return age >= lifetime;
    }

    public void setPosition(Util.Point position) {
        this.position = position;
    }

    public Util.Point getPosition() {
        return position;
    }

    public abstract void apply(PhysicsObject object);
}
