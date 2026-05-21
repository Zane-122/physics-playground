import java.awt.Graphics;

public class Polygon implements Drawable {

    int sides;
    double radius;
    Util.Point position;

    /**
     * Polygon that is defined in meters
     * @param sides
     * @param radius in meters
     * @param position in meters
     */
    public Polygon(int sides, double radius, Util.Point position) {
        this.sides = sides;
        this.radius = radius * Constants.pixelsPerMeter;
        this.position = position.toPixels();
    }

    public void setPosition(Util.Point p) {
        position = p;
    }

    @Override
    public void draw(Graphics g) {

        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];

        for (int i = 0; i < sides; i++) {

            double angle = 2 * Math.PI * ((double) i / sides);

            xPoints[i] = (int)(Math.cos(angle) * radius + position.x());
            yPoints[i] = (int)(Math.sin(angle) * radius + position.y());
        }

        g.drawPolygon(xPoints, yPoints, sides);
    }

    public Util.Point getPosition() {
        return position;
    }
}