import java.awt.Color;
import java.util.ArrayList;

public class ParticleEffect {
    private DrawingPanel panel;
    private Util.Point origin;
    private double lifespan = Constants.particleLifespan;
    private Color color = Color.getHSBColor((float) Math.random(), 1, 1);
    private ArrayList<PhysicsObject> particles;
    private int maxParticles = -1;
    private int particleCount;

    public ParticleEffect(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.origin = origin;
        this.particles = new ArrayList<>();
    }

    public ParticleEffect(DrawingPanel panel, Util.Point origin, Color color, double lifespan) {
        this.panel = panel;
        this.origin = origin;
        this.lifespan = lifespan;
        this.color = color;
        this.particles = new ArrayList<>();
    }

    public ParticleEffect(DrawingPanel panel, Util.Point origin, Color color, double lifespan, int maxParticles) {
        this.panel = panel;
        this.origin = origin;
        this.lifespan = lifespan;
        this.color = color;
        this.maxParticles = maxParticles;
        this.particles = new ArrayList<>();
    }

    public void addParticle() {
        if (maxParticles >= 0 && particleCount >= maxParticles) {
            return;
        }

        Util.Point spawnpoint = origin;

        Particle p = new Particle(spawnpoint, color, lifespan);

        double angle = -Math.toRadians(Math.random() * 360);
        double velocity = Math.random() * 3 + 5;

        double vx = Math.cos(angle) * velocity;
        double vy = Math.sin(angle) * velocity;
        PolygonHitbox hitbox = new PolygonHitbox(new Polygon(5, 8, spawnpoint));
        PhysicsObject po = new PhysicsObject(p, hitbox, spawnpoint, 1.0, vx, vy);
        po.setParticle(true);
        po.addComponent(new Gravity());

        particles.add(po);
        panel.addObject(po);

        particleCount++;
    }

    public boolean isFinished() {
        return maxParticles >= 0 && particleCount >= maxParticles && particles.isEmpty();
    }

    public void updateParticles(Simulation sim) {
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

    public void resolveCollisions(Simulation sim) {
        for (PhysicsObject p : particles) {
            p.resolveCollisions(sim);
        }
    }
}
