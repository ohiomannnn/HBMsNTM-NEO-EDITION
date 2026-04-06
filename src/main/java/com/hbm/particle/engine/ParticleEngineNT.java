package com.hbm.particle.engine;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.*;
import java.util.Map.Entry;

/**
 * Sure why not
 *
 * Just particle engine but it renders AFTER fucking clouds, so you can not use depth mask
 * @author ohiomannnn
 */
@OnlyIn(Dist.CLIENT)
public class ParticleEngineNT {

    public static final ParticleEngineNT INSTANCE = new ParticleEngineNT();

    private final List<ParticleNT> pendingParticles = new ArrayList<>();
    private final List<ParticleNT> particles = new ArrayList<>();

    public ParticleEngineNT() { }

    public void add(ParticleNT effect) {
        this.pendingParticles.add(effect);
    }

    public void clear() {
        this.pendingParticles.clear();
        this.particles.clear();
    }

    public void render(BufferSource buffer, Camera camera, DeltaTracker deltaTracker) {
        if (!this.particles.isEmpty()) {
            Map<RenderType, List<ParticleNT>> renderTypes = new HashMap<>();

            float f = deltaTracker.getGameTimeDeltaPartialTick(false);
            for (ParticleNT particle : this.particles) {
                if (particle == null || particle.dead) continue;
                RenderType type = particle.getRenderType();
                renderTypes.computeIfAbsent(type, t -> new ArrayList<>()).add(particle);
            }

            for (Entry<RenderType, List<ParticleNT>> entry : renderTypes.entrySet()) {
                RenderType type = entry.getKey();
                List<ParticleNT> particles = entry.getValue();

                VertexConsumer consumer = type != null ? buffer.getBuffer(type) : null;
                for (ParticleNT particle : particles) {
                    particle.render(consumer, camera, f);
                }
            }
        }
    }

    public void tick() {
        // we dont need to process empty lists
        if (this.particles.isEmpty() && pendingParticles.isEmpty()) return;

        if (!pendingParticles.isEmpty()) {
            particles.addAll(pendingParticles);
            pendingParticles.clear();
        }

        Iterator<ParticleNT> iterator = particles.iterator();
        while (iterator.hasNext()) {
            ParticleNT particle = iterator.next();

            particle.tick();
            if (particle.dead) {
                iterator.remove();
            }
        }

        if (!pendingParticles.isEmpty()) {
            particles.addAll(pendingParticles);
            pendingParticles.clear();
        }
    }
}
