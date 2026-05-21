public class PVector {
    private float x;
    private float y;

    public PVector(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Adds another vector to this vector.
     * @param v The vector to add to this vector.
     */
    public void add(PVector v) { x += v.x; y += v.y; }

    /**
     * Multiplies this vector by a scalar.
     * @param n The scalar to multiply this vector by.
     */
    public void mult(float n) { x *= n; y *= n; }

    /**
     * Returns the magnitude (length) of this vector.
     * @return The magnitude of this vector.
     */
    public float mag() { return (float) Math.sqrt(x*x + y*y); }

    /**
     * Normalizes this vector (makes it a unit vector / vector with length 1).
     */
    public void normalize() { float m = mag(); if (m != 0) { x /= m; y /= m; } }

    /**
     * Returns a random 2D vector.
     * @return A random 2D vector.
     */
    public static PVector random2D() {
        double angle = Math.random() * Math.PI * 2;
        return new PVector((float) Math.cos(angle), (float) Math.sin(angle));
    }
}
