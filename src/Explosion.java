public class Explosion extends Force {
    private static final double minimumDistance = 10;

    public Explosion(Util.Point position, double strength) {
        super(position, strength, 0);
    }

    @Override
    public void apply(PhysicsObject object) {
        if (object.isStatic()) return;

        double deltaX = object.getPosition().x() - position.x();
        double deltaY = object.getPosition().y() - position.y();
        double angle = Math.atan2(deltaY, deltaX);
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double velocity = strength / Math.max(distance, minimumDistance) / object.getMass();

        object.setVelocityX(Math.cos(angle) * velocity);
        object.setVelocityY(Math.sin(angle) * velocity);
    }
}
