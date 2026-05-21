import java.awt.Graphics;

public interface Drawable {
    void draw(Graphics g);
    void setPosition(Util.Point p);
    Util.Point getPosition();
}
