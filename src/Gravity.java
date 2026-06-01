public class Gravity extends Component {
    @Override
    public void update(PhysicsObject object, Simulation sim) {
       object.setVelocityY(sim.getGravityStep() + object.getVelocityY());
    }
}
