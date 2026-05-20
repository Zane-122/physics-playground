public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("SSup, World!");
        DrawingPanel drawingPanel = new DrawingPanel();

        Window myWindow = new Window(400, 300, "Physics Playground", drawingPanel);

        drawingPanel.addObject(new Polygon(6, 100, new Util.Point(100, 100)));
        myWindow.display();
    }
}
