import java.awt.Color;
import javax.swing.Timer;

public class App {
    private static final int explosionParticleCount = 90;

    public static void main(String[] args) throws Exception {
        Simulation sim = new Simulation();
        DrawingPanel drawingPanel = new DrawingPanel(sim);
        
        Window window = new Window(Constants.windowWidth, Constants.windowHeight, "Physics Playground", drawingPanel);
        window.display();

        ParticleSystem ps = new ParticleSystem(drawingPanel, new Util.Point(Constants.windowWidth / 2, Constants.windowHeight / 2), 1000);
        double w = Constants.windowWidth;
        double h = Constants.windowHeight;

        addBorderWall(sim, w * 2, new Util.Point(w / 2, h + 1300), 4);
        addBorderWall(sim, w * 2, new Util.Point(w / 2, h - 1700), 4);
        addBorderWall(sim, h * 2, new Util.Point(w + 1500, h / 2), 4);
        addBorderWall(sim, h * 2, new Util.Point(w - 1625, h / 2), 4);

        addBorderWall(sim, 80, new Util.Point(300,300), 3);
        addBorderWall(sim, 100, new Util.Point(1100,500), 5);

        Timer timer = new Timer((int)(1000 / Constants.FPS), e -> {
            Util.Point explosionPosition = drawingPanel.consumeExplosionSpawnPosition();
            if (explosionPosition != null) {
                sim.addForce(new Explosion(
                    explosionPosition,
                    drawingPanel.getExplosionStrength()
                ));
                ps.addParticleEffect(explosionPosition, Color.red, Constants.particleLifespan, explosionParticleCount);
            }

            ps.update(sim);
            sim.update();

            ps.resolveParticleCollisions(sim);
          
            drawingPanel.repaint();
        });

        timer.start();
    }

    private static void addBorderWall(Simulation sim, double size, Util.Point center, int sides) {
        Polygon square = new Polygon(sides, size, center, 0);
        PhysicsObject wall = new PhysicsObject(square, center, 0, Color.RED);
        wall.setRotation(Math.PI/4);
        wall.setStatic(true);
        wall.addComponent(new Freeze());
        sim.add(wall);
    }
}
