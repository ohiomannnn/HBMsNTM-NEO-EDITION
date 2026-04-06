package com.hbm.particle;

import com.hbm.main.NuclearTechMod;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MukeCloudBFParticle extends MukeCloudParticle {

    private static final ResourceLocation TEXTURE = NuclearTechMod.withDefaultNamespace("textures/particle_ss/explosion_bf.png");

    public MukeCloudBFParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd) {
        super(level, x, y, z, xd, yd, zd);
    }

    protected ResourceLocation getTexture() {
        return TEXTURE;
    }
}
