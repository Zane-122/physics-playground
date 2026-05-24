import java.awt.Graphics;
    

public class Polygon implements Drawable {

    protected int sides;
    protected double radius;
    protected Util.Point position;

    protected int[] xPoints = new int[sides];
    protected int[] yPoints = new int[sides];

    public Polygon(int sides, double radius, Util.Point position) {
        this.sides = sides;
        this.radius = radius;
        this.position = position;
    }

    public Polygon(Polygon polygon) {
        this.sides = polygon.getSides();
        this.radius = polygon.getRadius();
        this.position = polygon.getPosition();
    }

    public void setPosition(Util.Point p) {
        position = p;
    }

    @Override
    public void draw(Graphics g) {

        xPoints = new int[sides];
        yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {

            double angle = 2 * Math.PI * ((double) i / sides);

            xPoints[i] = (int)(Math.cos(angle) * radius + position.x());
            yPoints[i] = (int)(Math.sin(angle) * radius + position.y());
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