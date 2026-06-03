public class Push extends Force {
    public Push(Util.Point position, double strength) {
        super(position, strength);
    }

    public Push(Util.Point position, double strength, double lifetime) {
        super(position, strength, lifetime);
    }

    @Override
    public void apply(PhysicsObject object) {
        if (object.isStatic()) return;
        double angle = Math.atan2(object.getPosition().y() - position.y(), object.getPosition().x() - position.x()); 
        double distance = Math.sqrt(Math.pow(object.getPosition().x() - position.x(), 2) + Math.pow(object.getPosition().y() - position.y(), 2));
        distance = Math.max(distance, 10);
        double velocity = strength / (distance) / object.getMass() * Constants.timeStep;
        object.setVelocityX(Math.cos(angle) * velocity + object.getVelocityX());
        object.setVelocityY(Math.sin(angle) * velocity + object.getVelocityY());
    }
    
}
