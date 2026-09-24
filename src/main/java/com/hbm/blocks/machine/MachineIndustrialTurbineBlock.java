package com.hbm.blocks.machine;

import com.hbm.blockentity.ITickable;
import com.hbm.blockentity.ProxyComboBlockEntity;
import com.hbm.blockentity.machine.MachineIndustrialTurbineBlockEntity;
import com.hbm.blocks.DummyBlockType;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ILookOverlay;
import com.hbm.blocks.ITooltipProvider;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Coolable;
import com.hbm.registry.NtmSoundEvents;
import com.hbm.util.BobMathUtil;
import com.hbm.util.SoundUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MachineIndustrialTurbineBlock extends DummyableBlock implements ITooltipProvider, ILookOverlay {

    public MachineIndustrialTurbineBlock(Properties properties) {
        super(properties);
    }

    public static final MapCodec<MachineIndustrialTurbineBlock> CODEC = simpleCodec(MachineIndustrialTurbineBlock::new);
    @Override public MapCodec<MachineIndustrialTurbineBlock> codec() { return CODEC; }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        DummyBlockType type = state.getValue(TYPE);
        return switch(type) {
            case CORE -> new MachineIndustrialTurbineBlockEntity(pos, state);
            case EXTRA -> new ProxyComboBlockEntity(pos, state).fluid().power();
            default -> null;
        };
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(state.getValue(TYPE) != DummyBlockType.CORE) return null;
        return (lvl, pos, st, be) -> { if(be instanceof ITickable tickable) tickable.updateEntity(); };
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if(!player.isShiftKeyDown()) {
            BlockPos corePos = this.findCore(level, pos);
            if(corePos == null) return InteractionResult.FAIL;

            BlockEntity blockEntity = level.getBlockEntity(corePos);
            if(blockEntity instanceof MachineIndustrialTurbineBlockEntity be) {

                Direction dir = be.getBlockState().getValue(FACING);
                BlockPos bePos = be.getBlockPos();

                if(pos.getX() == bePos.getX() + dir.getStepX() * 3 && pos.getZ() == bePos.getZ() + dir.getStepZ() * 3 && pos.getY() == bePos.getY() + 1) {
                    if(!level.isClientSide) {
                        if(!be.operational) {
                            SoundUtils.playAtVec3(level, Vec3.atCenterOf(pos), NtmSoundEvents.TURBINE_LEVER.get(), SoundSource.BLOCKS, 1.5F, 1F);
                            be.onLeverPull();
                        } else {
                            player.sendSystemMessage(Component.literal("Cannot change compressor setting while operational!").withStyle(ChatFormatting.RED));
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override public int[] getDimensions() { return new int[] { 2, 0, 3, 3, 1, 1 }; }
    @Override public int getOffset() { return 3; }

    @Override
    protected void fillSpace(Level level, BlockPos pos, Direction dir, int offset) {
        super.fillSpace(level, pos, dir, offset);

        pos = pos.offset(dir.getStepX() * offset, 0, dir.getStepZ() * offset);

        Direction rot = dir.getClockWise(Axis.Y);

        this.makeExtra(level, pos.offset(dir.getStepX() * 3 + rot.getStepX(), 0, dir.getStepZ() * 3 + rot.getStepZ()));
        this.makeExtra(level, pos.offset(dir.getStepX() * 3 - rot.getStepX(), 0, dir.getStepZ() * 3 - rot.getStepZ()));
        this.makeExtra(level, pos.offset(-dir.getStepX() * 1 + rot.getStepX(), 0, -dir.getStepZ() * 1 + rot.getStepZ()));
        this.makeExtra(level, pos.offset(-dir.getStepX() * 1 - rot.getStepX(), 0, -dir.getStepZ() * 1 - rot.getStepZ()));
        this.makeExtra(level, pos.offset(dir.getStepX() * 3, 2, dir.getStepZ() * 3));
        this.makeExtra(level, pos.offset(-dir.getStepX() * 1, 2, -dir.getStepZ() * 1));
        this.makeExtra(level, pos.offset(-dir.getStepX() * 3, 1, -dir.getStepZ() * 3));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> components, TooltipFlag flag) {
        this.addStandardInfo(components);
    }

    private static final String[] BLOCKS = new String[] {"▖ ", "▘ ", " ▘", " ▖"}; // right hand side quarter blocks break the renderer so we cheat a little

    @Override
    public void printHook(RenderGuiEvent.Pre event, Level level, BlockPos pos) {
        BlockPos corePos = this.findCore(level, pos);
        if(corePos == null) return;

        BlockEntity blockEntity = level.getBlockEntity(corePos);
        if(blockEntity instanceof MachineIndustrialTurbineBlockEntity be) {
            List<Component> text = new ArrayList<>();

            FluidTank tankInput = be.tanks[0];
            FluidTank tankOutput = be.tanks[1];

            FluidType inputType = tankInput.getTankType();
            FluidType outputType = Fluids.NONE;

            if(inputType.hasTrait(FT_Coolable.class)) outputType = inputType.getTrait(FT_Coolable.class).coolsTo;

            int color = ((int) (0xFF - 0xFF * be.spin)) << 16 | ((int)(0xFF * be.spin) << 8);
            int time = (int) ((level.getGameTime() / 4) % 4);

            text.add(Component.literal("-> ").withStyle(ChatFormatting.GREEN).append(inputType.getName().append(": " + String.format(Locale.US, "%,d", tankInput.getFill()) + "/" + String.format(Locale.US, "%,d", tankInput.getMaxFill()) + "mB").withStyle(ChatFormatting.RESET)));
            text.add(Component.literal("<- ").withStyle(ChatFormatting.RED).append(outputType.getName().append(": " + String.format(Locale.US, "%,d", tankOutput.getFill()) + "/" + String.format(Locale.US, "%,d", tankOutput.getMaxFill()) + "mB").withStyle(ChatFormatting.RESET)));
            text.add(Component.literal("<- ").withStyle(ChatFormatting.RED).append(BobMathUtil.getShortNumber(be.powerBuffer) + "HE (").append(BLOCKS[be.powerBuffer <= 0 ? 0 : time] + (int) Math.round(be.spin * 100) + "%)").withColor(color));

            ILookOverlay.printGeneric(event, this.getName(), 0xffff00, 0x404000, text);
        }
    }
}
