package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.oil.MachineFrackingTowerBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.NuclearTechMod;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.loader.IModelCustom;
import com.hbm.render.util.RenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderFrackingTower extends BlockEntityRendererNT<MachineFrackingTowerBlockEntity> implements IBEWLRProvider {

    public static final ResourceLocation PIPE_TEX = NuclearTechMod.withDefaultNamespace("textures/block/pipe_silver.png");

    public static IModelCustom pipe;

    @Override
    public BlockEntityRenderer<MachineFrackingTowerBlockEntity> create(Context context) {

        if(pipe == null) pipe = ResourceManager.pipe_neo.asVBO();
        return new RenderFrackingTower();
    }

    @Override
    public void render(MachineFrackingTowerBlockEntity be, MultiBufferSource buffer, float partialTick) {

        RenderSystem.disableCull();
        RenderContext.translate(0.5F, 0F, 0.5F);
        RenderContext.mulPose(Axis.YP.rotationDegrees(180F));

        bindTexture(ResourceManager.FRACKING_TOWER_TEX);
        ResourceManager.fracking_tower.renderAll();

        RenderContext.translate(0F, 0.5F, 0F);

        bindTexture(PIPE_TEX);
        pipe.renderPart("pX");
        pipe.renderPart("nX");
        pipe.renderPart("pZ");
        pipe.renderPart("nZ");

        RenderSystem.enableCull();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.MACHINE_FRACKING_TOWER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -4.5F, 0F);
                RenderContext.scale(2.5F, 2.5F, 2.5F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.scale(0.25F, 0.25F, 0.25F);
                RenderSystem.disableCull();
                bindTexture(ResourceManager.FRACKING_TOWER_TEX); ResourceManager.fracking_tower.renderAll();
                RenderSystem.enableCull();
            }
        };
    }
}
