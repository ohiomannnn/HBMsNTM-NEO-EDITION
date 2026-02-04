package com.hbm.particle.engine;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Sure why not
 *
 * Just particle engine but it renders AFTER fucking clouds, so you can not use depth mask
 * @author ohiomannnn
 */
@OnlyIn(Dist.CLIENT)
public class ParticleEngineNT {

    public static final ParticleEngineNT INSTANCE = new ParticleEngineNT();

    private final List<ParticleNT> particles;

    public ParticleEngineNT() {
        this.particles = new ArrayList<>();
    }

    public void add(ParticleNT effect) {
        this.particles.add(effect);
    }

    public void clear() {
        this.particles.clear();
    }

    public void render(BufferSource buffer, Camera camera, DeltaTracker deltaTracker) {
        float f = deltaTracker.getGameTimeDeltaPartialTick(false);
        for (ParticleNT particle : particles) {
            VertexConsumer consumer = buffer.getBuffer(particle.getRenderType());
            particle.render(consumer, camera, f);
        }
    }

    public void tick() {
        this.tickParticleList(this.particles);
    }

    private void tickParticleList(List<ParticleNT> particles) {
        if (particles.isEmpty()) return;

        for (ParticleNT particle : new ArrayList<>(particles)) {
            if (particle == null) continue;
            particle.tick();
            if (particle.dead) {
                particles.remove(particle);
            }
        }
    }
}
