import java.awt.Graphics;
    

public class Polygon implements Drawable {

    protected int sides;
    protected double radius;
    protected Util.Point position;
    protected double rotation;

    protected int[] xPoints = new int[sides];
    protected int[] yPoints = new int[sides];

    public Polygon(int sides, double radius, Util.Point position) {
        this.sides = sides;
        this.radius = radius;
        this.position = position;
    }

    public Polygon(int sides, double radius, Util.Point position, double rotation) {
        this.sides = sides;
        this.radius = radius;
        this.position = position;
        this.rotation = rotation;
    }

    public Polygon(Polygon polygon) {
        this.sides = polygon.getSides();
        this.radius = polygon.getRadius();
        this.position = polygon.getPosition();
        this.rotation = polygon.getRotation();
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setPosition(Util.Point p) {
        position = p;
    }

    @Override
    public void draw(Graphics g) {

        xPoints = new int[sides];
        yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {

            double angle = 2 * Math.PI * ((double) i / sides) + rotation;

            xPoints[i] = (int) (Math.cos(angle) * radius + position.x());
            yPoints[i] = (int) (Math.sin(angle) * radius + position.y());
        }

        g.drawPolygon(xPoints, yPoints, sides);
    }

    @Override
    public Util.Point getPosition() {
        return position;
    }

    public int getSides() {
        return sides;
    }

    public double getRadius() {
        return radius;
    }
}