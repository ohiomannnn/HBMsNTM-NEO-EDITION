package com.hbm.particle;

import com.hbm.HBMsNTM;
import com.hbm.util.old.TessColorUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Random;

public class HazeParticle extends TextureSheetParticle {

    public HazeParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSpriteFromAge(ModParticles.HAZE_SPRITES);

        this.lifetime = 600 + random.nextInt(100);

        this.quadSize = 10F;
        this.alpha = 0;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age >= this.lifetime) {
            this.remove();
        }

        this.xd *= 0.96D;
        this.yd *= 0.96D;
        this.zd *= 0.96D;

        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }

        int x = (int)Math.floor(this.x) + random.nextInt(15) - 7;
        int z = (int)Math.floor(this.y) + random.nextInt(15) - 7;
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
        level.addParticle(ParticleTypes.LAVA, x + random.nextDouble(), y + 0.1, z + random.nextDouble(), 0.0, 0.0, 0.0);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();

        this.alpha = (float) Math.sin(age * Math.PI / (400F)) * 0.25F;

        int color = TessColorUtil.getColorRGBA_F(1.0F, 1.0F, 1.0F, alpha * 0.12F);

        Random rand = new Random(50);

        Vector3f up = new Vector3f(camera.getUpVector());
        Vector3f left = new Vector3f(camera.getLeftVector());

        for (int i = 0; i < 25; i++) {

            double dX = rand.nextGaussian() * 2.5D;
            double dY = rand.nextGaussian() * 0.15D;
            double dZ = rand.nextGaussian() * 2.5D;
            float size = (rand.nextFloat() * 0.25F + 0.75F) * quadSize;

            float pX = (float) ((float) (Mth.lerp(partialTicks, this.xo, this.x) - cameraPosition.x) + dX + rand.nextGaussian() * 0.5);
            float pY = (float) ((float) (Mth.lerp(partialTicks, this.yo, this.y) - cameraPosition.y) + dY + rand.nextGaussian() * 0.5);
            float pZ = (float) ((float) (Mth.lerp(partialTicks, this.zo, this.z) - cameraPosition.z) + dZ + rand.nextGaussian() * 0.5);

            renderQuad(consumer, pX, pY, pZ, up, left, size, color);
        }
    }

    private void renderQuad(VertexConsumer consumer, float cx, float cy, float cz, Vector3f up, Vector3f left, float scale, int color) {

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        Vector3f l = new Vector3f(left).mul(scale);
        Vector3f u = new Vector3f(up).mul(scale);

        consumer.addVertex(cx - l.x - u.x, cy - l.y - u.y, cz - l.z - u.z)
                .setUv(u1, v1)
                .setColor(color)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx - l.x + u.x, cy - l.y + u.y, cz - l.z + u.z)
                .setUv(u1, v0)
                .setColor(color)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx + l.x + u.x, cy + l.y + u.y, cz + l.z + u.z)
                .setUv(u0, v0)
                .setColor(color)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
        consumer.addVertex(cx + l.x - u.x, cy + l.y - u.y, cz + l.z - u.z)
                .setUv(u0, v1)
                .setColor(color)
                .setNormal(0.0F, 1.0F, 0.0F)
                .setLight(240);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return CustomRenderType.FOG;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Provider(SpriteSet sprites) {
            ModParticles.HAZE_SPRITES = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HazeParticle(level, x, y, z);
        }
    }
}
