import java.util.ArrayList;
import java.awt.Color;

public class ParticleSystem {
    private ArrayList<ParticleEffect> particleEffects;
    private DrawingPanel panel;
    private int intensity;
    private double spawnAccumulator;
    private static final int FPS = 60;

    public ParticleSystem(DrawingPanel panel, Util.Point origin, int intensity) {
        this.panel = panel;
        this.particleEffects = new ArrayList<>();
        this.intensity = intensity;
        this.spawnAccumulator = 0.0;
    }

    public void addParticleEffect(Color color, double lifespan, int maxParticles) {
        if (panel.wasClicked()) {
            Util.Point effectPosition = panel.getClickPosition();
            ParticleEffect effect = new ParticleEffect(panel, effectPosition, color, lifespan, maxParticles);
            particleEffects.add(effect);
        }
    }

    public void update(Simulation sim) {
        spawnAccumulator += (double) intensity / FPS;
        int particlesToSpawn = (int) spawnAccumulator;
        if (particlesToSpawn > 0) {
            spawnAccumulator -= particlesToSpawn;
        }

        for (ParticleEffect effect : particleEffects) {
            for (int i = 0; i < particlesToSpawn; i++) {
                effect.addParticle();
            }
            effect.update(sim);
        }

        particleEffects.removeIf(ParticleEffect::isFinished);
    }
}
