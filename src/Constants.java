public class Constants {
    public static final int pixelsPerMeter = 1;
    public static final double gravity = 9.81 * pixelsPerMeter;
    public static final double FPS = 60;
    public static final double timeStep = 1.0 / FPS;
    public static final double gravityStep = gravity * timeStep;

    public static final double windowWidth = 800;
    public static final double windowHeight = 600;

    public static final float particleLifespan = 150.0f;
}
