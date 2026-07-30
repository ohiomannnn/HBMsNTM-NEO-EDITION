package com.hbm.blockentity.machine.oil;

import com.hbm.blockentity.IUpgradeInfoProvider;
import com.hbm.blockentity.NtmBlockEntityTypes;
import com.hbm.blocks.DummyableBlock;
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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MachinePumpjackBlockEntity extends OilDrillBaseBlockEntity {

    // todo config
    protected static int maxPower = 250_000;
    protected static int consumption = 200;
    protected static int delay = 25;
    protected static int oilPerDepsoit = 750;
    protected static int gasPerDepositMin = 50;
    protected static int gasPerDepositMax = 250;
    protected static double drainChance = 0.025;

    public float rot = 0;
    public float prevRot = 0;
    public float speed = 0;

    public MachinePumpjackBlockEntity(BlockPos pos, BlockState state) {
        super(NtmBlockEntityTypes.MACHINE_PUMPJACK.get(), pos, state);
    }

    @Override protected Component getDefaultName() { return Component.translatable("container.pumpjack"); }

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

        BlockPos origin = this.getBlockPos();
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
    public void updateEntity() {
        super.updateEntity();
        if(this.level == null) return;

        if(this.level.isClientSide) {

            this.prevRot = rot;

            if(this.indicator == 0) {
                this.rot += speed;
            }

            if(this.rot >= 360) {
                this.prevRot -= 360;
                this.rot -= 360;
            }
        }
    }


    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        super.serialize(buf);

        buf.writeFloat(this.indicator == 0 ? (5F + (2F * this.speedLevel)) + (this.overLevel - 1F) * 10: 0F);
    }

    @Override
    public void deserialize(RegistryFriendlyByteBuf buf) {
        super.deserialize(buf);

        this.speed = buf.readFloat();
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

        Direction dir = this.getBlockState().getValue(DummyableBlock.FACING);
        Direction rot = dir.getCounterClockWise(Direction.Axis.Y);

        BlockPos pos = this.getBlockPos();
        return new DirPos[] {
                new DirPos(pos.getX() + rot.getStepX() * 2 + dir.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 2 + dir.getStepZ() * 2, dir),
                new DirPos(pos.getX() + rot.getStepX() * 2 + dir.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 4 - dir.getStepZ() * 2, dir.getOpposite()),
                new DirPos(pos.getX() + rot.getStepX() * 4 - dir.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 4 + dir.getStepZ() * 2, dir),
                new DirPos(pos.getX() + rot.getStepX() * 4 - dir.getStepX() * 2, pos.getY(), pos.getZ() + rot.getStepZ() * 2 - dir.getStepZ() * 2, dir.getOpposite())
        };
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MachineOilWellMenu<>(id, inventory, this);
    }

    @Override
    public void provideInfo(UpgradeType type, int lvl, List<Component> components, TooltipFlag flag) {
        components.add(IUpgradeInfoProvider.getStandardLabel(NtmBlocks.MACHINE_PUMPJACK.get()));
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
