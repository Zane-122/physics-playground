public class PolygonHitbox extends Polygon {
    public PolygonHitbox(int sides, double radius, Util.Point position) {
        super(sides, radius, position);
    }

    public PolygonHitbox(Polygon polygon) {
        super(polygon);
    }

    public boolean isColliding(PolygonHitbox other) {
        Util.Point[] pointsA = getPoints(this);
        Util.Point[] pointsB = getPoints(other);
        return !(hasSeparatingAxis(pointsA, pointsB) || hasSeparatingAxis(pointsB, pointsA));
    }

    private static Util.Point[] getPoints(Polygon poly) {
        Util.Point[] points = new Util.Point[poly.getSides()];
        for (int i = 0; i < poly.getSides(); i++) {
            double angle = 2 * Math.PI * ((double) i / poly.getSides());
            double x = Math.cos(angle) * poly.getRadius() + poly.getPosition().x();
            double y = Math.sin(angle) * poly.getRadius() + poly.getPosition().y();
            points[i] = new Util.Point(x, y);
        }
        return points;
    }

    private static boolean hasSeparatingAxis(Util.Point[] polyA, Util.Point[] polyB) {
        int n = polyA.length;
        for (int i = 0; i < n; i++) {
            Util.Point a1 = polyA[i];
            Util.Point a2 = polyA[(i + 1) % n];

            double axisX = -(a2.y() - a1.y());
            double axisY =   a2.x() - a1.x();

            double[] projA = project(polyA, axisX, axisY);
            double[] projB = project(polyB, axisX, axisY);

            if (projA[1] < projB[0] || projB[1] < projA[0]) return true;
        }
        return false;
    }
    private static double[] project(Util.Point[] poly, double axisX, double axisY) {
        double min = axisX * poly[0].x() + axisY * poly[0].y();
        double max = min;
        for (Util.Point p : poly) {
            double proj = axisX * p.x() + axisY * p.y();
            if (proj < min) min = proj;
            if (proj > max) max = proj;
        }
        return new double[]{min, max};
    }
}
