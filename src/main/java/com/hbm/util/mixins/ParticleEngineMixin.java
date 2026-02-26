package com.hbm.util.mixins;

import com.hbm.blocks.DummyableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Shadow protected ClientLevel level;
    @Final @Shadow private RandomSource random;

    @Shadow public abstract void add(Particle particle);

    @Inject(method = "crack", at = @At("HEAD"), cancellable = true)
    private void crack(BlockPos pos, Direction direction, CallbackInfo ci) {
        BlockState blockstate = this.level.getBlockState(pos);

        if (blockstate.getBlock() instanceof DummyableBlock) {
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();

                AABB aabb = Shapes.block().bounds();

                double d0 = i + this.random.nextDouble() * (aabb.maxX - aabb.minX - 0.2) + 0.1 + aabb.minX;
                double d1 = j + this.random.nextDouble() * (aabb.maxY - aabb.minY - 0.2) + 0.1 + aabb.minY;
                double d2 = k + this.random.nextDouble() * (aabb.maxZ - aabb.minZ - 0.2) + 0.1 + aabb.minZ;

                if (direction == Direction.DOWN)  d1 = j + aabb.minY - 0.1;
                if (direction == Direction.UP)    d1 = j + aabb.maxY + 0.1;
                if (direction == Direction.NORTH) d2 = k + aabb.minZ - 0.1;
                if (direction == Direction.SOUTH) d2 = k + aabb.maxZ + 0.1;
                if (direction == Direction.WEST)  d0 = i + aabb.minX - 0.1;
                if (direction == Direction.EAST)  d0 = i + aabb.maxX + 0.1;

                this.add(new TerrainParticle(this.level, d0, d1, d2, 0.0, 0.0, 0.0, blockstate, pos).updateSprite(blockstate, pos).setPower(0.2F));
            }
            ci.cancel();
        }
    }

    @Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
    private void destroy(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (this.level.getBlockState(pos).getBlock() instanceof DummyableBlock) {
            Shapes.block().forAllBoxes((p_172273_, p_172274_, p_172275_, p_172276_, p_172277_, p_172278_) -> {
                double d1 = Math.min(1.0D, p_172276_ - p_172273_);
                double d2 = Math.min(1.0D, p_172277_ - p_172274_);
                double d3 = Math.min(1.0D, p_172278_ - p_172275_);
                int i = Math.max(2, Mth.ceil(d1 / (double)0.25F));
                int j = Math.max(2, Mth.ceil(d2 / (double)0.25F));
                int k = Math.max(2, Mth.ceil(d3 / (double)0.25F));

                for (int l = 0; l < i; ++l) {
                    for (int i1 = 0; i1 < j; ++i1) {
                        for (int j1 = 0; j1 < k; ++j1) {
                            double d4 = ((double)l + (double)0.5F) / (double)i;
                            double d5 = ((double)i1 + (double)0.5F) / (double)j;
                            double d6 = ((double)j1 + (double)0.5F) / (double)k;
                            double d7 = d4 * d1 + p_172273_;
                            double d8 = d5 * d2 + p_172274_;
                            double d9 = d6 * d3 + p_172275_;
                            this.add((new TerrainParticle(this.level, (double)pos.getX() + d7, (double)pos.getY() + d8, (double)pos.getZ() + d9, d4 - (double)0.5F, d5 - (double)0.5F, d6 - (double)0.5F, state, pos)).updateSprite(state, pos));
                        }
                    }
                }

            });
            ci.cancel();
        }
    }
}
