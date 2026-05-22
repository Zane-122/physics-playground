import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<PhysicsObject> particles;
    private DrawingPanel panel;
    private Util.Point origin;

    public ParticleSystem(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.origin = origin;
        this.particles = new ArrayList<>();
    }
    
    public void addParticle() {
        if (!panel.isMouseHeld() || panel.getMousePos() == null) return;

        Util.Point spawnpoint = panel.getMousePos();
        Particle p = new Particle(spawnpoint);

        double angle = -Math.toRadians(Math.random() * 60 + 240);
        double velocity = Math.random() * 3 + 1;

        double vx = Math.cos(angle) * velocity;
        double vy = Math.sin(angle) * velocity;
        PolygonHitbox hitbox = (PolygonHitbox) new Polygon(5, 8, spawnpoint);
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
                panel.removeObject(visual);
                particles.remove(i);
            }
        }
    }
}