import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<ParticleEffect> particleEffects;
    private DrawingPanel panel;

    public ParticleSystem(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.particleEffects = new ArrayList<>();
    }

        Util.Point spawnpoint = panel.getMousePos();
        Particle p = new Particle(spawnpoint);

        double angle = -Math.toRadians(Math.random() * 60 + 240);
        double velocity = Math.random() * 3 + 1;

        double vx = Math.cos(angle) * velocity;
        double vy = Math.sin(angle) * velocity;
        PolygonHitbox hitbox = new PolygonHitbox(new Polygon(5, 8, spawnpoint));
        PhysicsObject po = new PhysicsObject(p, hitbox, spawnpoint, 1.0, vx, vy);
        po.addComponent(new Gravity());
        po.addComponent(new Collision());

        particles.add(po);
        panel.addObject(po);
    public void addParticleEffect() {
        if (panel.wasClicked()) {
            Util.Point effectPosition = panel.getClickPosition();
            ParticleEffect effect = new ParticleEffect(panel, effectPosition);
            particleEffects.add(effect);
        }
    }

    public void update(Simulation sim) {
        for (ParticleEffect effect : particleEffects) {
            effect.addParticle();
            effect.update(sim);
        }
    }
}
