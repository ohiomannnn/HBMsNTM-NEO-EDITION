package com.hbm.blocks.machine;

import com.hbm.blockentity.IPersistentNBT;
import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.machine.oil.MachineOilWellBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.IPersistentInfoProvider;
import com.hbm.explosion.vanillant.ExplosionVNT;
import com.hbm.explosion.vanillant.standard.BlockAllocatorStandard;
import com.hbm.explosion.vanillant.standard.BlockProcessorStandard;
import com.hbm.explosion.vanillant.standard.EntityProcessorCross;
import com.hbm.handler.MultiblockHandlerXR;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.particle.helper.ExplosionCreator;
import com.hbm.util.BobMathUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class MachineOilWellBlock extends DummyableBlock implements IPersistentInfoProvider {

    public MachineOilWellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        if(type == DummyBlockType.CORE) return new MachineOilWellBlockEntity(pos, state);
        return null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    public static final MapCodec<MachineOilWellBlock> CODEC = simpleCodec(MachineOilWellBlock::new);
    @Override public MapCodec<MachineOilWellBlock> codec() { return CODEC; }

    @Override public int[] getDimensions() { return new int[] { 9, 0, 1, 1, 1, 1 }; }
    @Override public int getOffset() { return 0; }

    @Override
    protected boolean checkRequirement(Level level, BlockPos pos, Direction dir, int offset) {
        return MultiblockHandlerXR.checkSpace(level, pos, new int[] {1, -1, 0, 0, 0, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.offset(0, 1, 0), new int[] {8, 0, 1, 1, 1, 1}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.offset(1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.offset(1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.offset(-1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir) &&
                MultiblockHandlerXR.checkSpace(level, pos.offset(-1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, pos, dir);
    }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        MultiblockHandlerXR.fillSpace(level, pos, new int[] {1, -1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(0, 1, 0), new int[] {8, 0, 1, 1, 1, 1}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(-1, 1, 1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
        MultiblockHandlerXR.fillSpace(level, pos.offset(-1, 1, -1), new int[] {-1, 1, 0, 0, 0, 0}, this, dir);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return this.standardOpenBehavior(level, pos, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return IPersistentNBT.getDropsFromLootParams(state, params);
    }

    @Override
    public void appendHoverText(ItemStack stack, CompoundTag persistentTag, List<Component> components, Item.TooltipContext context, TooltipFlag flag) {

        components.add(Component.literal(BobMathUtil.getShortNumber(persistentTag.getLong("power")) + "HE").withStyle(ChatFormatting.GREEN));
        for(int i = 0; i < 2; i++) {
            FluidTank tank = new FluidTank(Fluids.NONE, 0);
            tank.readFromNBT(persistentTag, "t" + i);
            components.add(Component.literal(tank.getFill() + "/" + tank.getMaxFill() + "mB ").append(tank.getTankType().getName()).withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {

        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;
        BlockEntity core = level.getBlockEntity(corePos);
        if(!(core instanceof MachineOilWellBlockEntity be)) return;

        level.removeBlock(pos, false);

        if(be.tanks[0].getFill() > 0 || be.tanks[1].getFill() > 0) {
            be.tanks[0].setFill(0);
            be.tanks[1].setFill(0);

            ExplosionVNT xnt = new ExplosionVNT(level, corePos.getX() + 0.5, corePos.getY() + 0.5, corePos.getZ() + 0.5, 15F)
                    .setBlockAllocator(new BlockAllocatorStandard(24))
                    .setBlockProcessor(new BlockProcessorStandard().setNoDrop())
                    .setEntityProcessor(new EntityProcessorCross(0.5));
            xnt.explode();

            ExplosionCreator.composeEffect(level, corePos.getX() + 0.5, corePos.getY() + 0.5, corePos.getZ() + 0.5, 10, 2F, 0.5F, 25F, 5, 8, 20, 0.75F, 1F, -2F, 150);
        }
    }
}
