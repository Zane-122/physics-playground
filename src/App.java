import javax.swing.Timer;

public class App {
    public static void main(String[] args) throws Exception {
        Simulation sim = new Simulation();
        DrawingPanel drawingPanel = new DrawingPanel(sim);
        
        Window window = new Window(Constants.windowWidth, Constants.windowHeight, "Physics Playground", drawingPanel);
        window.display();

        ParticleSystem ps = new ParticleSystem(drawingPanel, new Util.Point(Constants.windowWidth / 2, Constants.windowHeight / 2));
        PhysicsObject obj = new PhysicsObject(new Polygon(4, 50, new Util.Point(100, 100)), new Util.Point(100, 100), 1);
        sim.add(obj);

        Timer timer = new Timer((int)(1000 / Constants.FPS), e -> {
            ps.update(sim);

            sim.updatePhysicsObjects();

            drawingPanel.repaint();
        });

        timer.start();
    }
}
