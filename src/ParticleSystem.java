import java.awt.Color;
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
        Particle p = new Particle(spawnpoint, Color.getHSBColor((float) Math.random(), 1, 1));

        double angle = -Math.toRadians(Math.random() * 360);
        double velocity = Math.random() * 3 + 5;

        double vx = Math.cos(angle) * velocity;
        double vy = Math.sin(angle) * velocity;

        PhysicsObject po = new PhysicsObject(p, spawnpoint, 1.0, vx, vy);
        po.update();
        particles.add(po);
        panel.addObject(po);
    }

    public void update() {  
        for (int i = particles.size() - 1; i >= 0; i--) {
            PhysicsObject p = particles.get(i);
            Particle visual = (Particle) p.getVisual();

            p.update();
            visual.fade();

            if (visual.isDead()) {
                panel.removeObject(visual);
                particles.remove(i);
            }
        }
    }

    public void spawnParticles(int count) {
        for (int i = 0; i < count; i++) {
            addParticle();
        }
    }
}