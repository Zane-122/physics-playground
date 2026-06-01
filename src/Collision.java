public class Collision extends Component{
    @Override
    public void update(PhysicsObject object, Simulation sim) {
        for (PhysicsObject obj : sim.getObjects()) {
            if (obj.equals(object)) continue;
            
            if (obj.isColliding(object)) {
                System.out.println("COLLISION HAPPENING!");
            }
        }
    }
}
