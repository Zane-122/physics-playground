public class Freeze extends Component {
    Util.Point position;

    @Override
    public void initialize(PhysicsObject object) {
        position = object.getPosition();
    }

    @Override
    public void update(PhysicsObject object, Simulation sim) {
        object.setPosition(position);
    }
}
