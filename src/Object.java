public abstract class Object {
    private Util.Point position;
    private Util.Vector velocity;
    private double mass;

    public Object(Util.Point p, Util.Vector v, double m) {
        position = p;
        velocity = v;
        mass = m;
    }

    
}
