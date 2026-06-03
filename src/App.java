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
        double borderSize = Math.max(w, h);

        addBorderWall(sim, borderSize, new Util.Point(w / 2, -borderSize));
        addBorderWall(sim, borderSize, new Util.Point(w / 2, h + borderSize));
        addBorderWall(sim, borderSize, new Util.Point(-borderSize, h / 2));
        addBorderWall(sim, borderSize, new Util.Point(w + borderSize, h / 2));

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
          
            drawingPanel.repaint();
        });

        timer.start();
    }

    private static void addBorderWall(Simulation sim, double size, Util.Point center) {
        Polygon square = new Polygon(4, size, center, 0);
        PhysicsObject wall = new PhysicsObject(square, center, 0);
        wall.setRotation(Math.PI/4);
        wall.setStatic(true);
        wall.addComponent(new Freeze());
        sim.add(wall);
    }
}
