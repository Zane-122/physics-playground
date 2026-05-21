public class Util {
    public static record Point(double x, double y) {
        public Point toPixels() {
            return new Point(x * Constants.pixelsPerMeter, y * Constants.pixelsPerMeter);
        }

        public Point toMeters() {
            return new Point(x / Constants.pixelsPerMeter, y / Constants.pixelsPerMeter);
        }
    }
    public static record Vector(double x, double y){};
}
