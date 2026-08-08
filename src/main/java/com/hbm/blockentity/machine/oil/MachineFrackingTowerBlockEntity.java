package com.hbm.blockentity.machine.oil;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.NtmBlocks;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.menus.MachineOilWellMenu;
import com.hbm.items.machine.MachineUpgradeItem.UpgradeType;
import com.hbm.util.BobMathUtil;
import com.hbm.util.fauxpointtwelve.DirPos;
import com.hbm.world.feature.OilSpot;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MachineFrackingTowerBlockEntity extends OilDrillBaseBlockEntity {

    // todo config
    protected static int maxPower = 5_000_000;
    protected static int consumption = 5000;
    protected static int solutionRequired = 10;
    protected static int delay = 20;
    protected static int oilPerDepsoit = 1000;
    protected static int gasPerDepositMin = 100;
    protected static int gasPerDepositMax = 500;
    protected static double drainChance = 0.02;
    protected static int oilPerBedrockDepsoit = 100;
    protected static int gasPerBedrockDepositMin = 10;
    protected static int gasPerBedrockDepositMax = 50;
    protected static int destructionRange = 75;

    public MachineFrackingTowerBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_FRACKING_TOWER.get(), pos, state);

        this.tanks = new FluidTank[3];
        this.tanks[0] = new FluidTank(Fluids.OIL, 64_000);
        this.tanks[1] = new FluidTank(Fluids.GAS, 64_000);
        this.tanks[2] = new FluidTank(Fluids.FRACKSOL, 64_000);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.fracking_tower"); }

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
    public int getDrillDepth() {
        return 0;
    }

    @Override
    public boolean canPump() {
        boolean b = this.tanks[2].getFill() >= solutionRequired;

        if(!b) {
            this.indicator = 3;
        }

        return b;
    }

    @Override
    public boolean canSuckBlock(BlockState state) {
        return super.canSuckBlock(state) || state.is(NtmBlocks.ORE_BEDROCK_OIL.get());
    }

    @Override
    public void doSuck(BlockPos pos) {
        super.doSuck(pos);

        if(this.level == null) return;
        if(this.level.getBlockState(pos).is(NtmBlocks.ORE_BEDROCK_OIL.get())) {
            this.onSuck(pos);
        }
    }

    @Override
    public void onSuck(BlockPos pos) {
        if(this.level == null) return;

        BlockState state = this.level.getBlockState(pos);

        int oil = 0;
        int gas = 0;

        if(state.is(NtmBlocks.ORE_OIL.get())) {
            oil = oilPerDepsoit;
            gas = gasPerDepositMin + this.level.random.nextInt(gasPerDepositMax - gasPerDepositMin + 1);

            if(level.random.nextDouble() < drainChance) {
                level.setBlock(pos, NtmBlocks.ORE_OIL_EMPTY.get().defaultBlockState(), 3);
            }
        }
        if(state.is(NtmBlocks.ORE_BEDROCK_OIL.get())) {
            oil = oilPerBedrockDepsoit;
            gas = gasPerBedrockDepositMin + this.level.random.nextInt(gasPerBedrockDepositMax - gasPerBedrockDepositMin + 1);
        }

        this.tanks[0].setFill(this.tanks[0].getFill() + oil);
        if(this.tanks[0].getFill() > this.tanks[0].getMaxFill()) this.tanks[0].setFill(tanks[0].getMaxFill());
        this.tanks[1].setFill(this.tanks[1].getFill() + gas);
        if(this.tanks[1].getFill() > this.tanks[1].getMaxFill()) this.tanks[1].setFill(tanks[1].getMaxFill());

        this.tanks[2].setFill(tanks[2].getFill() - solutionRequired);

        OilSpot.generateOilSpot(this.level, this.getBlockPos().getX(), this.getBlockPos().getZ(), destructionRange, 10, false);
    }

    @Override
    public FluidTank[] getSendingTanks() {
        return new FluidTank[] { tanks[0], tanks[1] };
    }

    @Override
    public FluidTank[] getReceivingTanks() {
        return new FluidTank[] { tanks[2] };
    }

    @Override
    public FluidTank[] getAllTanks() {
        return tanks;
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
    protected void updateConnections() {
        if(this.level == null) return;

        for(DirPos pos : this.getConPos()) {
            this.trySubscribe(level, pos);
            this.trySubscribe(tanks[2].getTankType(), level, pos);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineOilWellMenu<>(id, inventory, this);
    }

    @Override
    public void provideInfo(UpgradeType type, int lvl, List<Component> components, TooltipFlag flag) {
        components.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_FRACKING_TOWER.get()));
        if(type == UpgradeType.SPEED) {
            components.add(Component.translatable(KEY_DELAY, "-" + (lvl * 25) + "%").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable(KEY_CONSUMPTION, "+" + (lvl * 25) + "%").withStyle(ChatFormatting.RED));
        }
        if(type == UpgradeType.POWER) {
            components.add(Component.translatable(KEY_CONSUMPTION, "-" + (lvl * 25) + "%").withStyle(ChatFormatting.GREEN));
            components.add(Component.translatable(KEY_DELAY, "+" + (lvl * 10) + "%").withStyle(ChatFormatting.RED));
        }
        if(type == UpgradeType.AFTERBURN) {
            components.add(Component.translatable(KEY_BURN, lvl * 10, lvl * 50).withStyle(ChatFormatting.GREEN));
        }
        if(type == UpgradeType.OVERDRIVE) {
            components.add(Component.translatable(KEY_YES).withStyle(BobMathUtil.getBlink() ? ChatFormatting.RED : ChatFormatting.DARK_GRAY));
        }
    }
}
