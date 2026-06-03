public class Freeze extends Component {
    Util.Point position;
    private double angle;
    private double angularVelocity;

    @Override
    public void initialize(PhysicsObject object) {
        position = object.getPosition();
        angle = object.getAngle();
        angularVelocity = object.getAngularVelocity();
    }

    @Override
    public void update(PhysicsObject object, Simulation sim) {
        object.setPosition(position);
        object.setAngle(angle);
        object.setAngularVelocity(angularVelocity);
    }
}
