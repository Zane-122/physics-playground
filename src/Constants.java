public class Constants {
    public static final int pixelsPerMeter = 1;
    public static final double gravity = 9.81 * pixelsPerMeter;
    public static final double FPS = 40;
    public static final double timeStep = 1.0 / FPS;
    public static final double gravityStep = gravity * timeStep;

    public static final double windowWidth = 800;
    public static final double windowHeight = 600;

    public static final double particleLifespan = 20;

    /** 0 = no bounce, 1 = perfectly elastic */
    public static final double restitution = 0.5;

    /** Contact friction retention applied on static contact tangential velocity. */
    public static final double contactFrictionRetention = 0.99;

    public static final double separationSlop = 2.0;
    public static final int collisionIterations = 3;
}
