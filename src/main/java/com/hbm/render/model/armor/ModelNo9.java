package com.hbm.render.model.armor;

import com.hbm.main.ResourceManager;
import com.hbm.render.loader.ModelRendererObj;
import com.hbm.render.util.FullBright;
import com.hbm.render.util.RenderContext;
import com.hbm.util.TagsUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ModelNo9 extends ModelArmorBase {

    public final ModelRendererObj lamp;
    public final ModelRendererObj insignia;

    public ModelNo9(HumanoidModel<? extends LivingEntity> model, EquipmentSlot slot) {
        super(model, slot);

        this.head = new ModelRendererObj(ResourceManager.armor_no9, "Helmet").copyRotationFrom(model.head);
        this.insignia = new ModelRendererObj(ResourceManager.armor_no9, "Insignia");
        this.lamp = new ModelRendererObj(ResourceManager.armor_no9, "Flame");
    }

    @Override
    public void render(boolean head) {

        this.head.copyTo(this.insignia);
        this.head.copyTo(this.lamp);

        if(this.slot == EquipmentSlot.HEAD && head) {
            RenderSystem.disableCull();

            bindTexture(ResourceManager.NO9_TEX);
            this.head.render(0.07F);
            bindTexture(ResourceManager.NO9_INSIGNIA_TEX);
            this.insignia.render(0.07F);

            if(this.living != null) {
                if(this.living instanceof Player player) {

                    ItemStack helmet = player.getItemBySlot(this.slot);

                    if(TagsUtil.getCustomData(helmet).getBoolean("isOn")) {
                        bindTexture(ResourceManager.WHITE_TEX);
                        FullBright.enable();
                        RenderContext.setLightning(false);
                        RenderSystem.setShaderColor(1F, 1F, 0.8F, 1F);
                        this.lamp.render(0.07F);
                        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
                        RenderContext.setLightning(true);
                        FullBright.disable();
                    }
                }
            }

            RenderSystem.enableCull();
        }
    }
}
