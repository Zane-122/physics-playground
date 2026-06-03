import java.awt.Color;

import javax.swing.Timer;

public class App {
    public static void main(String[] args) throws Exception {
        Simulation sim = new Simulation();
        DrawingPanel drawingPanel = new DrawingPanel(sim);
        
        Window window = new Window(Constants.windowWidth, Constants.windowHeight, "Physics Playground", drawingPanel);
        window.display();

        ParticleSystem ps = new ParticleSystem(drawingPanel, new Util.Point(Constants.windowWidth / 2, Constants.windowHeight / 2), 10);
        double w = Constants.windowWidth;
        double h = Constants.windowHeight;
        double borderSize = Math.max(w, h);

        addBorderWall(sim, borderSize, new Util.Point(w / 2, -borderSize));
        addBorderWall(sim, borderSize, new Util.Point(w / 2, h + borderSize));
        addBorderWall(sim, borderSize, new Util.Point(-borderSize, h / 2));
        addBorderWall(sim, borderSize, new Util.Point(w + borderSize, h / 2));

        Timer timer = new Timer((int)(1000 / Constants.FPS), e -> {
            ps.addParticleEffect(Color.red, Constants.particleLifespan, 10);
            ps.update(sim);

            sim.updatePhysicsObjects();

            drawingPanel.repaint();
        });

        timer.start();
    }

    private static void addBorderWall(Simulation sim, double size, Util.Point center) {
        Polygon square = new Polygon(4, size, center);
        PhysicsObject wall = new PhysicsObject(square, center, 0);
        wall.addComponent(new Freeze());
        wall.addComponent(new Collision());
        sim.add(wall);
    }
}
