package com.hbm.blocks.bomb;

import com.hbm.blockentity.BlockEntityNT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.ICustomBlockModelRegister;
import com.hbm.blocks.MultiBlock;
import com.hbm.blocks.NtmBlocks;
import com.hbm.entity.projectile.Shrapnel;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockMutatorDebris;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.inventory.MetaHelper;
import com.hbm.network.toclient.AuxParticle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class VolcanoBlock extends MultiBlock implements EntityBlock, ICustomBlockModelRegister {

    public VolcanoBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new VolcanoCoreBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (lvl, pos, st, be) -> { if (be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override public int getSubCount() { return 5; }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        int meta = MetaHelper.getMeta(stack);

        if(meta == META_SMOLDERING) {
            components.add(Component.translatable("block.hbmsntm.obj_volcano.desc0").withStyle(ChatFormatting.GOLD));
            return;
        }

        components.add(VolcanoBlock.isGrowing(meta) ? Component.translatable("block.hbmsntm.obj_volcano.desc1").withStyle(ChatFormatting.RED) : Component.translatable("block.hbmsntm.obj_volcano.desc2").withStyle(ChatFormatting.DARK_GRAY));
        components.add(VolcanoBlock.isExtinguishing(meta) ? Component.translatable("block.hbmsntm.obj_volcano.desc3").withStyle(ChatFormatting.RED) : Component.translatable("block.hbmsntm.obj_volcano.desc4").withStyle(ChatFormatting.DARK_GRAY));
    }

    public static final int META_STATIC_ACTIVE = 0;
    public static final int META_STATIC_EXTINGUISHING = 1;
    public static final int META_GROWING_ACTIVE = 2;
    public static final int META_GROWING_EXTINGUISHING = 3;
    public static final int META_SMOLDERING = 4;

    public static boolean isGrowing(int meta) {
        return meta == META_GROWING_ACTIVE || meta == META_GROWING_EXTINGUISHING;
    }

    public static boolean isExtinguishing(int meta) {
        return meta == META_STATIC_EXTINGUISHING || meta == META_GROWING_EXTINGUISHING;
    }

    @Override
    public void registerModel(BlockStateProvider provider, ResourceLocation modelLocation) {
        provider.simpleBlockWithItem(this, provider.cubeAll(this));
    }

    public static class VolcanoCoreBlockEntity extends BlockEntityNT implements ITickable {

        public int volcanoTimer;

        public VolcanoCoreBlockEntity(BlockPos pos, BlockState state) {
            super(NtmBlockEntityTypes.VOLCANO_CORE.get(), pos, state);
        }

        @Override
        public void updateEntity() {
            if(this.level == null) return;

            if(!this.level.isClientSide) {
                this.volcanoTimer++;

                if(this.volcanoTimer % 10 == 0) {
                    //if that type has a vertical channel, blast it open and raise the magma
                    if(this.hasVerticalChannel()) {
                        this.blastMagmaChannel();
                        this.raiseMagma();
                    }

                    double magmaChamber = this.magmaChamberSize();
                    if(magmaChamber > 0) this.blastMagmaChamber(magmaChamber);

                    Object[] melting = this.surfaceMeltingParams();
                    if(melting != null) this.meltSurface((int)melting[0], (double)melting[1], (double)melting[2]);

                    //self-explanatory
                    if(this.isSpewing()) this.spawnBlobs();
                    if(this.isSmoking()) this.spawnSmoke();

                    this.surroundLava();
                }

                if(this.volcanoTimer >= this.getUpdateRate()) {
                    this.volcanoTimer = 0;

                    if(this.shouldGrow()) {
                        level.setBlock(this.getBlockPos().above(), this.getBlockState(), 3);
                        level.setBlock(this.getBlockPos(), this.getLava(), 3);
                    } else if(this.isExtinguishing()) {
                        level.setBlock(this.getBlockPos(), this.getLava(), 3);
                    }
                }
            }
        }

        public boolean isRadioacitve() {
            return this.getBlockState().is(NtmBlocks.VOLCANO_RAD_CORE);
        }

        protected BlockState getLava() {
            if(isRadioacitve()) return NtmBlocks.RAD_LAVA.get().defaultBlockState();
            return NtmBlocks.VOLCANIC_LAVA.get().defaultBlockState();
        }

        @Override
        protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.loadAdditional(tag, registries);

            this.volcanoTimer = tag.getInt("Timer");
        }

        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.saveAdditional(tag, registries);

            tag.putInt("Timer", this.volcanoTimer);
        }

        private boolean shouldGrow() {
            return isGrowing() && this.getBlockPos().getY() < 200;
        }

        private boolean isGrowing() {
            int meta = this.getMeta();
            return meta == META_GROWING_ACTIVE || meta == META_GROWING_EXTINGUISHING;
        }

        private boolean isExtinguishing() {
            int meta = this.getMeta();
            return meta == META_STATIC_EXTINGUISHING || meta == META_GROWING_EXTINGUISHING;
        }

        private boolean isSmoking() {
            return this.getMeta() != META_SMOLDERING;
        }

        private boolean isSpewing() {
            return this.getMeta() != META_SMOLDERING;
        }

        private boolean hasVerticalChannel() {
            return this.getMeta() != META_SMOLDERING;
        }

        private double magmaChamberSize() {
            return this.getMeta() == META_SMOLDERING ? 15 : 0;
        }

        /* count per tick, radius, depth */
        private Object[] surfaceMeltingParams() {
            return this.getMeta() == META_SMOLDERING ? new Object[] { 50, 50D, 10D } : null;
        }

        private int getUpdateRate() {
            return switch(this.getMeta()) {
                case META_STATIC_EXTINGUISHING -> 60 * 60 * 20; //once per hour
                case META_GROWING_ACTIVE, META_GROWING_EXTINGUISHING -> 60 * 60 * 20 / 250; //250x per hour
                default -> 10;
            };
        }

        /** Causes two magma explosions, one from bedrock to the core and one from the core to 15 blocks above. */
        private void blastMagmaChannel() {
            if(this.level == null) return;
            BlockPos pos = this.getBlockPos();

            ExplosionVNT explosion = new ExplosionVNT(this.level, pos.getX() + 0.5, pos.getY() + level.random.nextInt(15) + 1.5, pos.getZ() + 0.5, 7F)
                    .setBlockAllocator(new BlockAllocatorStandard())
                    .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(new BlockMutatorDebris(this.getLava())));
            explosion.explode();
            ExplosionVNT explosion2 = new ExplosionVNT(this.level, pos.getX() + 0.5 + level.random.nextGaussian() * 3, level.random.nextIntBetweenInclusive(level.getMinBuildHeight(), pos.getY()), pos.getZ() + 0.5 + level.random.nextGaussian() * 3, 10F)
                    .setBlockAllocator(new BlockAllocatorStandard())
                    .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(new BlockMutatorDebris(this.getLava())));
            explosion2.explode();

        }

        /** Causes two magma explosions at a random position around the core, one at normal and one at half range. */
        private void blastMagmaChamber(double size) {
            if(this.level == null) return;
            BlockPos pos = this.getBlockPos();

            for(int i = 0; i < 2; i++) {
                double dist = size / (double) (i + 1);

                ExplosionVNT explosion = new ExplosionVNT(this.level, pos.getX() + 0.5 + level.random.nextGaussian() * dist, pos.getY() + 0.5 + level.random.nextGaussian() * dist, pos.getZ() + 0.5 + level.random.nextGaussian() * dist, 7F)
                        .setBlockAllocator(new BlockAllocatorStandard())
                        .setBlockProcessor(new BlockProcessorStandard().setNoDrop().withBlockEffect(new BlockMutatorDebris(this.getLava())));
                explosion.explode();
            }
        }

        /** Randomly selects surface blocks and converts them into lava if solid or air if not solid. */
        private void meltSurface(int count, double radius, double depth) {
            if(this.level == null) return;

            BlockPos pos = this.getBlockPos();
            int x = pos.getX();
            int z = pos.getZ();

            for(int i = 0; i < count; i++) {
                x = (int) Math.floor(x + level.random.nextGaussian() * radius);
                z = (int) Math.floor(z + level.random.nextGaussian() * radius);
                //gaussian distribution makes conversions more likely on the surface and rarer at the bottom
                int y = level.getHeight(Types.MOTION_BLOCKING, x, z) + 1 - (int) Math.floor(Math.abs(level.random.nextGaussian() * depth));

                BlockPos targetPos = new BlockPos(x, y, z);
                BlockState b = level.getBlockState(targetPos);

                if(!b.isAir() && b.getBlock().getExplosionResistance() < Blocks.OBSIDIAN.getExplosionResistance()) {
                    //turn into lava if solid block, otherwise just break
                    level.setBlock(targetPos, b.isSolidRender(level, targetPos) ? this.getLava() : Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        /** Increases the magma level in a small radius around the core. */
        private void raiseMagma() {
            if(this.level == null) return;

            BlockPos rPos = this.getBlockPos().offset(
                    -(10 + level.random.nextInt(21)),
                    level.random.nextInt(11),
                    -(10 + level.random.nextInt(21))
            );

            if(level.getBlockState(rPos).isAir() && level.getBlockState(rPos.below()) == this.getLava()) {
                level.setBlock(rPos, this.getLava(), 3);
            }
        }

        /** Creates a 3x3x3 lava sphere around the core. */
        private void surroundLava() {
            if(this.level == null) return;

            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    for(int k = -1; k <= 1; k++) {

                        if(i != 0 || j != 0 || k != 0) {
                            level.setBlock(this.getBlockPos().offset(i, j, k), this.getLava(), 3);
                        }
                    }
                }
            }
        }

        /** Spews specially tagged shrapnels which create volcanic lava and monoxide clouds. */
        private void spawnBlobs() {
            if(this.level == null) return;

            BlockPos pos = this.getBlockPos();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for(int i = 0; i < 3; i++) {
                Shrapnel frag = new Shrapnel(level);
                frag.moveTo(x + 0.5, y + 1.5, z + 0.5, 0.0F, 0.0F);
                frag.setDeltaMovement(level.random.nextGaussian() * 0.2D, 1D + level.random.nextDouble(), level.random.nextGaussian() * 0.2D);
                if(this.isRadioacitve()) {
                    frag.setRadVolcano(true);
                } else {
                    frag.setVolcano(true);
                }
                level.addFreshEntity(frag);
            }
        }

        /** I SEE SMOKE, AND WHERE THERE'S SMOKE THERE'S FIRE! */
        private void spawnSmoke() {
            if(this.level == null) return;

            CompoundTag tag = new CompoundTag();
            tag.putString("type", "vanillaExt");
            tag.putString("mode", "volcano");
            BlockPos pos = this.getBlockPos().offset(0, 10, 0);
            if(level instanceof ServerLevel serverLevel) {
                PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 250, new AuxParticle(tag, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
            }
        }
    }
}
