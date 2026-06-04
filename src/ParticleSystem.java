import java.awt.Color;
import java.util.ArrayList;

public class ParticleSystem {
    public ArrayList<ParticleEffect> particleEffects;
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
            addParticleEffect(panel.getClickPosition(), color, lifespan, maxParticles);
        }
    }

    public void addParticleEffect(Util.Point effectPosition, Color color, double lifespan, int maxParticles) {
        ParticleEffect effect = new ParticleEffect(panel, effectPosition, color, lifespan, maxParticles);
        particleEffects.add(effect);
    }

    public void addParticleEffect(Util.Point effectPosition, Color color, double lifespan, int maxParticles, double direction) {
        ParticleEffect effect = new ParticleEffect(panel, effectPosition, color, lifespan, maxParticles, direction);
        particleEffects.add(effect);
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
            effect.updateParticles(sim);
        }

        particleEffects.removeIf(ParticleEffect::isFinished);
    }

    public void resolveParticleCollisions(Simulation sim) {
        for (ParticleEffect effect : particleEffects) {
            effect.resolveCollisions(sim);
        }

        particleEffects.removeIf(ParticleEffect::isFinished);
    }
}
