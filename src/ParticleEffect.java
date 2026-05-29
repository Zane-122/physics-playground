import java.awt.Color;
import java.util.ArrayList;

public class ParticleEffect {
    private DrawingPanel panel;
    private Util.Point origin;
    private ArrayList<PhysicsObject> particles;

    public ParticleEffect(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.origin = origin;
        this.particles = new ArrayList<>();
    }

    public void addParticle() {
        Util.Point spawnpoint = origin;
        Particle p = new Particle(spawnpoint, Color.getHSBColor((float) Math.random(), 1, 1));

        double angle = -Math.toRadians(Math.random() * 360);
        double velocity = Math.random() * 3 + 5;

        double vx = Math.cos(angle) * velocity;
        double vy = Math.sin(angle) * velocity;
        PolygonHitbox hitbox = new PolygonHitbox(new Polygon(5, 8, spawnpoint));
        PhysicsObject po = new PhysicsObject(p, hitbox, spawnpoint, 1.0, vx, vy);

        particles.add(po);
        panel.addObject(po);
    }

    public void update(Simulation sim) {
        for (int i = particles.size() - 1; i >= 0; i--) {
            PhysicsObject p = particles.get(i);
            Particle visual = (Particle) p.getVisual();

            p.update(sim);
            visual.fade();

            if (visual.isDead()) {
                panel.removeObject(p);
                particles.remove(i);
            }
        }
    }
}
