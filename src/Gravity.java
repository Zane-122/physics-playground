public class Gravity extends Component {
    @Override
    public void update(PhysicsObject object, Simulation sim) {
        object.setPosition(new Util.Point(object.getPosition().x(), object.getPosition().y() + sim.getGravityStep()));
    }
}
