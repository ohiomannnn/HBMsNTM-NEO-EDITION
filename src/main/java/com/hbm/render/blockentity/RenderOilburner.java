package com.hbm.render.blockentity;

import com.hbm.blockentity.machine.heater.HeaterOilburnerBlockEntity;
import com.hbm.blocks.NtmBlocks;
import com.hbm.main.ResourceManager;
import com.hbm.render.item.ItemRenderBase;
import com.hbm.render.util.RenderContext;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RenderOilburner extends BlockEntityRendererNT<HeaterOilburnerBlockEntity> implements IBEWLRProvider {

    @Override public BlockEntityRenderer<HeaterOilburnerBlockEntity> create(Context context) { return new RenderOilburner(); }

    @Override
    public void render(HeaterOilburnerBlockEntity be, MultiBufferSource buffer, float partialTicks) {

        RenderContext.translate(0.5F, 0.0F, 0.5F);

        bindTexture(ResourceManager.HEATER_OILBURNER_TEX);
        ResourceManager.heater_oilburner.renderAll();
    }

    @Override
    public Item getItemForRenderer() {
        return NtmBlocks.HEATER_OILBURNER.asItem();
    }

    @Override
    public BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRenderBase() {
            @Override
            public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
                RenderContext.translate(0F, -1.5F, 0F);
                RenderContext.scale(3.25F, 3.25F, 3.25F);
            }

            @Override
            public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
                bindTexture(ResourceManager.HEATER_OILBURNER_TEX);
                ResourceManager.heater_oilburner.renderAll();
            }
        };
    }
}
