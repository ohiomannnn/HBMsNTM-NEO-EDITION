package com.hbm.blocks.gas;

import com.hbm.HBMsNTMClient;
import com.hbm.util.ArmorUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class GasBaseBlock extends Block {

    protected float red;
    protected float green;
    protected float blue;

    public GasBaseBlock(Properties properties, float r, float g, float b) {
        super(properties);

        this.red = r;
        this.green = g;
        this.blue = b;
    }

    @Override public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) { return Shapes.empty(); }
    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.INVISIBLE; }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            level.scheduleTick(pos, this, 10);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean isMoving) {
        level.scheduleTick(pos, this, 10);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!tryMove(level, pos, getFirstDirection(level, pos))) {
            if (!tryMove(level, pos, getSecondDirection(level, pos))) {
                level.scheduleTick(pos, this, getDelay(level));
            }
        }
    }

    public abstract Direction getFirstDirection(Level level, BlockPos pos);

    public Direction getSecondDirection(Level level, BlockPos pos) {
        return getFirstDirection(level, pos);
    }

    public boolean tryMove(ServerLevel level, BlockPos pos, Direction dir) {
        BlockPos targetPos = pos.relative(dir);

        if (level.getBlockState(targetPos).isAir()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            level.setBlock(targetPos, this.defaultBlockState(), 2);
            level.scheduleTick(targetPos, this, getDelay(level));
            return true;
        }

        return false;
    }

    public int getDelay(LevelAccessor levelAccessor) {
        return 2;
    }

    public Direction randomHorizontal(RandomSource random) {
        return Direction.values()[random.nextInt(4) + 2];
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (ArmorUtil.checkArmorPiece(player, Items.GOLDEN_HELMET, EquipmentSlot.HEAD)) {
            CompoundTag tag = new CompoundTag();
            tag.putString("type", "vanillaExt");
            tag.putString("mode", "cloud");
            tag.putDouble("posX", pos.getX() + 0.5);
            tag.putDouble("posY", pos.getY() + 0.5);
            tag.putDouble("posZ", pos.getZ() + 0.5);
            tag.putFloat("r", red);
            tag.putFloat("g", green);
            tag.putFloat("b", blue);
            HBMsNTMClient.effectNT(tag);
        }
    }
}