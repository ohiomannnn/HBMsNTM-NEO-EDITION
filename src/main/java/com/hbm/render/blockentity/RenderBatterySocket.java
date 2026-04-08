package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.storage.BatterySocketBlockEntity;
import com.hbm.blocks.DummyableBlock;
import com.hbm.blocks.ModBlocks;
import com.hbm.inventory.MetaHelper;
import com.hbm.items.NtmItems;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.BeamPronter;
import com.hbm.render.util.BeamPronter.BeamType;
import com.hbm.render.util.BeamPronter.WaveType;
import com.hbm.render.util.HorsePronter;
import com.hbm.render.util.RenderContext;
import com.hbm.util.EnumUtil;
import com.hbm.util.Vec3NT;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class RenderBatterySocket extends BlockEntityRendererNT<BatterySocketBlockEntity> implements IBEWLRProvider {

    private static final ResourceLocation blorbo = NuclearTechMod.withDefaultNamespace("textures/models/horse/sunburst.png");

    @Override public BlockEntityRenderer<BatterySocketBlockEntity> create(Context context) { return new RenderBatterySocket(); }

    @Override
    public void render(BatterySocketBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

        Direction facing = be.getBlockState().getValue(DummyableBlock.FACING);
        float rot = switch (facing) {
            case UP, DOWN -> 0.0F;
            case NORTH -> 270f;
            case SOUTH -> 90f;
            case WEST -> 0f;
            case EAST -> 180f;
        };

        int tPackedLight = LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos().above(1));

        RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.BATTERY_SOCKET_TEX), poseStack, tPackedLight, packedOverlay);
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(rot));
        RenderContext.translate(0.5F, 0, -0.5F);

        ResourceManager.battery_socket.renderPart("Socket");

        ItemStack render = be.syncStack;
        if (!render.isEmpty()) {
            if (render.is(NtmItems.BATTERY_PACK.get())) {
                BatteryPackType pack = EnumUtil.grabEnumSafely(BatteryPackType.class, MetaHelper.getMeta(render));
                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(pack.texture));
                ResourceManager.battery_socket.renderPart(pack.isCapacitor() ? "Capacitor" : "Battery");
            } else if (render.is(NtmItems.BATTERY_SC.get())) {
                RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(ResourceManager.BATTERY_SC_TEX));
                ResourceManager.battery_socket.renderPart("Battery");
            } else if (render.is(NtmItems.BATTERY_CREATIVE.get())) {
                RenderContext.pushPose();
                RenderContext.setRenderType(NtmRenderTypes.FVBO_NC.apply(blorbo));
                RenderContext.scale(0.75F, 0.75F, 0.75F);
                RenderContext.mulPose(Axis.YN.rotationDegrees((be.getLevel().getGameTime() % 360 + partialTicks) * 25F));

                HorsePronter.reset();
                HorsePronter.enableHorn();
                HorsePronter.pront();
                RenderContext.popPose();

                Random rand = new Random(be.getLevel().getGameTime() / 5);
                rand.nextBoolean();

                for (int i = -1; i <= 1; i += 2) {
                    for (int j = -1; j <= 1; j += 2) {
                        if (rand.nextInt(4) == 0) {
                            RenderContext.pushPose();
                            RenderContext.translate(0, 0.75, 0);
                            BeamPronter.prontBeam(new Vec3NT(0.4375 * i, 1.1875, 0.4375 * j), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int) (System.currentTimeMillis() % 1000) / 50, 15, 0.0625F, 3, 0.025F);
                            BeamPronter.prontBeam(new Vec3NT(0.4375 * i, 1.1875, 0.4375 * j), WaveType.RANDOM, BeamType.SOLID, 0x404040, 0x002040, (int) (System.currentTimeMillis() % 1000) / 50, 1, 0, 3, 0.025F);
                            RenderContext.popPose();
                        }
                    }
                }
            }
        }

        RenderContext.end();
    }

    @Override
    public Item getItemForRenderer() {
        return ModBlocks.MACHINE_BATTERY_SOCKET.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                poseStack.translate(0F, -2F, 0F);
                poseStack.scale(5F, 5F, 5F);
            }

            @Override
            public void renderCommon(ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                RenderContext.setup(NtmRenderTypes.FVBO.apply(ResourceManager.BATTERY_SOCKET_TEX), poseStack, packedLight, packedOverlay);
                ResourceManager.battery_socket.renderPart("Socket");
                RenderContext.end();
            }
        };
    }
}
