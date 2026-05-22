import javax.swing.Timer;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("SSup, World!");
        DrawingPanel drawingPanel = new DrawingPanel();

        Window window = new Window(Constants.windowWidth, Constants.windowHeight, "Physics Playground", drawingPanel);
        window.display();

        ParticleSystem ps = new ParticleSystem(drawingPanel, new Util.Point(Constants.windowWidth / 2, Constants.windowHeight / 2));
        PhysicsObject obj = new PhysicsObject(new Polygon(4, 50, new Util.Point(100, 100)), new Util.Point(100, 100), 1);
        drawingPanel.addObject(obj);

        Timer timer = new Timer((int)(1000 / Constants.FPS), e -> {
            ps.addParticle();  // spawn one particle per frame
            ps.update();
            obj.update();
            
            drawingPanel.repaint();
        });

        timer.start();
    }
}
