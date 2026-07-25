package com.hbm.blockentity.machine.oil;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.menus.MachineOilWellMenu;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.util.BobMathUtil;
import com.hbm.util.SoundUtils;
import com.hbm.util.fauxpointtwelve.DirPos;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MachineOilWellBlockEntity extends OilDrillBaseBlockEntity {

    // todo config
    protected static long maxPower = 100_000;
    protected static int consumption = 100;
    protected static int delay = 50;
    protected static int oilPerDepsoit = 500;
    protected static int gasPerDepositMin = 100;
    protected static int gasPerDepositMax = 500;
    protected static double drainChance = 0.05;

    public MachineOilWellBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_OIL_WELL.get(), pos, state);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.oil_well"); }

    @Override
    public long getMaxPower() {
        return maxPower;
    }

    @Override
    public int getPowerReq() {
        return consumption;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public void onDrill(int y) {
        if(this.level == null) return;

        Block block = this.level.getBlockState(new BlockPos(this.getBlockPos().getX(), y, this.getBlockPos().getZ())).getBlock();
        String path = BuiltInRegistries.BLOCK.getKey(block).getPath();

        Block gas = null;
        if(path.contains("uranium")) {
            gas = NtmBlocks.GAS_RADON_DENSE.get();
        } else if(path.contains("asbestos")) {
            gas = NtmBlocks.GAS_ASBESTOS.get();
        }

        if(gas == null) return;

        BlockPos origin = this.getBlockPos().above(10);
        for(int dx = -1; dx <= 1; dx++) {
            for(int dz = -1; dz <= 1; dz++) {
                BlockPos target = origin.offset(dx, 0, dz);
                if(this.level.getBlockState(target).canBeReplaced()) {
                    this.level.setBlock(target, gas.defaultBlockState(), 3);
                }
            }
        }
    }

    @Override
    public void onSuck(BlockPos pos) {
        if(this.level == null) return;

        SoundUtils.playAtVec3(this.level, Vec3.atLowerCornerOf(this.getBlockPos()), SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS, 2F, 0.5F);

        this.tanks[0].setFill(this.tanks[0].getFill() + oilPerDepsoit);
        if(this.tanks[0].getFill() > this.tanks[0].getMaxFill()) this.tanks[0].setFill(tanks[0].getMaxFill());
        this.tanks[1].setFill(this.tanks[1].getFill() + (gasPerDepositMin + level.random.nextInt((gasPerDepositMax - gasPerDepositMin + 1))));
        if(this.tanks[1].getFill() > this.tanks[1].getMaxFill()) this.tanks[1].setFill(tanks[1].getMaxFill());

        if(level.random.nextDouble() < drainChance) {
            level.setBlock(pos, NtmBlocks.ORE_OIL_EMPTY.get().defaultBlockState(), 3);
        }
    }

    @Override
    public DirPos[] getConPos() {
        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.offset(1, 0, 0), Direction.EAST),
                new DirPos(pos.offset(-1, 0, 0), Direction.WEST),
                new DirPos(pos.offset(1, 0, 1), Direction.SOUTH),
                new DirPos(pos.offset(1, 0, -1), Direction.NORTH)
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineOilWellMenu(id, inventory, this);
    }

    @Override
    public void provideInfo(UpgradeType type, int level, List<Component> components, boolean extendedInfo) {
        components.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_WELL.get()));
        if(type == UpgradeType.SPEED) {
            components.add(Component.translatable(KEY_DELAY, "-" + (level * 25) + "%").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable(KEY_CONSUMPTION, "+" + (level * 25) + "%").withStyle(ChatFormatting.RED));
        }
        if(type == UpgradeType.POWER) {
            components.add(Component.translatable(KEY_CONSUMPTION, "-" + (level * 25) + "%").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable(KEY_DELAY, "+" + (level * 10) + "%").withStyle(ChatFormatting.RED));
        }
        if(type == UpgradeType.AFTERBURN) {
            components.add(Component.translatable(KEY_BURN, level * 10, level * 50).withStyle(ChatFormatting.GREEN));
        }
        if(type == UpgradeType.OVERDRIVE) {
            components.add(Component.translatable(KEY_YES).withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY));
        }
    }
}
