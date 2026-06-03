public class PolygonHitbox extends Polygon {
    public PolygonHitbox(int sides, double radius, Util.Point position) {
        super(sides, radius, position);
    }

    public PolygonHitbox(Polygon polygon) {
        super(polygon);
    }

    public boolean isColliding(PolygonHitbox other) {
        return getCollisionData(other) != null;
    }

    public CollisionData getCollisionData(PolygonHitbox other) {
        Util.Point[] pointsA = getPoints(this);
        Util.Point[] pointsB = getPoints(other);

        double minDepth = Double.MAX_VALUE;
        double bestAxisX = 0;
        double bestAxisY = 0;

        for (int pass = 0; pass < 2; pass++) {
            Util.Point[] poly = pass == 0 ? pointsA : pointsB;
            int n = poly.length;
            for (int i = 0; i < n; i++) {
                Util.Point a1 = poly[i];
                Util.Point a2 = poly[(i + 1) % n];

                double axisX = -(a2.y() - a1.y());
                double axisY = a2.x() - a1.x();
                double len = Math.hypot(axisX, axisY);
                if (len == 0) continue;
                axisX /= len;
                axisY /= len;

                double depth = overlapOnAxis(pointsA, pointsB, axisX, axisY);
                if (depth < 0) return null;
                if (depth < minDepth) {
                    minDepth = depth;
                    bestAxisX = axisX;
                    bestAxisY = axisY;
                }
            }
        }

        Util.Point centerA = centroid(pointsA);
        Util.Point centerB = centroid(pointsB);
        double dx = centerA.x() - centerB.x();
        double dy = centerA.y() - centerB.y();
        if (dx * bestAxisX + dy * bestAxisY < 0) {
            bestAxisX = -bestAxisX;
            bestAxisY = -bestAxisY;
        }

        Util.Point contact = findContactPoint(pointsA, pointsB, bestAxisX, bestAxisY);
        return new CollisionData(bestAxisX, bestAxisY, minDepth, contact.x(), contact.y());
    }

    private static Util.Point findContactPoint(
        Util.Point[] pointsA,
        Util.Point[] pointsB,
        double nx,
        double ny
    ) {
        Util.Point centerB = centroid(pointsB);
        double best = Double.NEGATIVE_INFINITY;
        Util.Point contact = pointsA[0];

        for (Util.Point p : pointsA) {
            double penetration = (p.x() - centerB.x()) * nx + (p.y() - centerB.y()) * ny;
            if (penetration > best) {
                best = penetration;
                contact = p;
            }
        }

        for (Util.Point p : pointsB) {
            double penetration = (centerB.x() - p.x()) * nx + (centerB.y() - p.y()) * ny;
            if (penetration > best) {
                best = penetration;
                contact = p;
            }
        }

        return contact;
    }

    private static double overlapOnAxis(Util.Point[] polyA, Util.Point[] polyB, double axisX, double axisY) {
        double[] projA = project(polyA, axisX, axisY);
        double[] projB = project(polyB, axisX, axisY);
        if (projA[1] < projB[0] || projB[1] < projA[0]) return -1;
        return Math.min(projA[1], projB[1]) - Math.max(projA[0], projB[0]);
    }

    private static Util.Point centroid(Util.Point[] points) {
        double cx = 0;
        double cy = 0;
        for (Util.Point p : points) {
            cx += p.x();
            cy += p.y();
        }
        return new Util.Point(cx / points.length, cy / points.length);
    }

    private static Util.Point[] getPoints(Polygon poly) {
        Util.Point[] points = new Util.Point[poly.getSides()];
        for (int i = 0; i < poly.getSides(); i++) {
            double angle = 2 * Math.PI * ((double) i / poly.getSides()) + poly.getRotation();
            double x = Math.cos(angle) * poly.getRadius() + poly.getPosition().x();
            double y = Math.sin(angle) * poly.getRadius() + poly.getPosition().y();
            points[i] = new Util.Point(x, y);
        }
        return points;
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
