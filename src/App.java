public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("SSup, World!");
        DrawingPanel drawingPanel = new DrawingPanel();

        Window myWindow = new Window(400, 300, "Physics Playground", drawingPanel);

        PhysicsObject obj = new PhysicsObject(new Polygon(4, 50, new Util.Point(100, 100)), new Util.Point(100, 100), 1);
        drawingPanel.addObject(obj);
        myWindow.display();

        while (true) {
            obj.update();
            drawingPanel.repaint();
            
            Thread.sleep(16);
        }
    }
}
