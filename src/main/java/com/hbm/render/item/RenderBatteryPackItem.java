package com.hbm.render.item;

import com.hbm.inventory.MetaHelper;
import com.hbm.items.machine.BatteryPackItem.BatteryPackType;
import com.hbm.main.ResourceManager;
import com.hbm.render.NtmRenderTypes;
import com.hbm.render.util.RenderContext;
import com.hbm.util.EnumUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;

public class RenderBatteryPackItem extends ItemRenderBase {

    @Override
    public void renderInventory(ItemStack stack, MultiBufferSource buffer) {
        RenderContext.translate(0F, -3F, 0F);
        RenderContext.scale(5F, 5F, 5F);
    }

    @Override
    public void renderCommon(ItemStack stack, MultiBufferSource buffer) {
        BatteryPackType pack = EnumUtil.grabEnumSafely(BatteryPackType.class, MetaHelper.getMeta(stack));

        RenderContext.setRenderType(NtmRenderTypes.FVBO.apply(pack.texture));
        ResourceManager.battery_socket.renderPart(pack.isCapacitor() ? "Capacitor" : "Battery");
    }
}
