import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<ParticleEffect> particleEffects;
    private DrawingPanel panel;

    public ParticleSystem(DrawingPanel panel, Util.Point origin) {
        this.panel = panel;
        this.particleEffects = new ArrayList<>();
    }

    public void spawnAndUpdateParticles(Simulation sim) {
        // if (panel.wasClicked()) {
        //     Util.Point effectPosition = panel.getClickPosition();
        //     ParticleEffect effect = new ParticleEffect(panel, effectPosition);
        //     particleEffects.add(effect);
        // }

        for (ParticleEffect effect : particleEffects) {
            effect.addParticle();
            effect.updateParticles(sim);
        }
    }

    public void resolveParticleCollisions(Simulation sim) {
        for (ParticleEffect effect : particleEffects) {
            effect.resolveCollisions(sim);
        }
    }
}
