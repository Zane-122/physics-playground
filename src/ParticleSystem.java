import java.awt.Color;
import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<PhysicsObject> particles;
    private ArrayList<ParticleEffect> particleEffects;
    private DrawingPanel panel;
    private Util.Point origin;

    public ParticleSystem(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.origin = origin;
        this.particles = new ArrayList<>();
        this.particleEffects = new ArrayList<>();
    }

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