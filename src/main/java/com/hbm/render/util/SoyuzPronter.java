package com.hbm.render.util;

import com.hbm.main.ResourceManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;

public class SoyuzPronter {

    public enum SoyuzSkin {

        SOYUZ(
                ResourceManager.SOYUZ_ENGINEBLOCK,
                ResourceManager.SOYUZ_BOTTOMSTAGE,
                ResourceManager.SOYUZ_TOPSTAGE,
                ResourceManager.SOYUZ_PAYLOAD,
                ResourceManager.SOYUZ_PAYLOADBLOCKS,
                ResourceManager.SOYUZ_LES,
                ResourceManager.SOYUZ_LESTHRUSTERS,
                ResourceManager.SOYUZ_MAINENGINES,
                ResourceManager.SOYUZ_SIDEENGINES,
                ResourceManager.SOYUZ_BOOSTER,
                ResourceManager.SOYUZ_BOOSTERSIDE
        ),
        LUNA(
                ResourceManager.SOYUZ_LUNA_ENGINEBLOCK,
                ResourceManager.SOYUZ_LUNA_BOTTOMSTAGE,
                ResourceManager.SOYUZ_LUNA_TOPSTAGE,
                ResourceManager.SOYUZ_LUNA_PAYLOAD,
                ResourceManager.SOYUZ_LUNA_PAYLOADBLOCKS,
                ResourceManager.SOYUZ_LUNA_LES,
                ResourceManager.SOYUZ_LUNA_LESTHRUSTERS,
                ResourceManager.SOYUZ_LUNA_MAINENGINES,
                ResourceManager.SOYUZ_LUNA_SIDEENGINES,
                ResourceManager.SOYUZ_LUNA_BOOSTER,
                ResourceManager.SOYUZ_LUNA_BOOSTERSIDE
        ),
        AUTHENTIC(
                ResourceManager.SOYUZ_AUTHENTIC_ENGINEBLOCK,
                ResourceManager.SOYUZ_AUTHENTIC_BOTTOMSTAGE,
                ResourceManager.SOYUZ_AUTHENTIC_TOPSTAGE,
                ResourceManager.SOYUZ_AUTHENTIC_PAYLOAD,
                ResourceManager.SOYUZ_AUTHENTIC_PAYLOADBLOCKS,
                ResourceManager.SOYUZ_AUTHENTIC_LES,
                ResourceManager.SOYUZ_AUTHENTIC_LESTHRUSTERS,
                ResourceManager.SOYUZ_AUTHENTIC_MAINENGINES,
                ResourceManager.SOYUZ_AUTHENTIC_SIDEENGINES,
                ResourceManager.SOYUZ_AUTHENTIC_BOOSTER,
                ResourceManager.SOYUZ_AUTHENTIC_BOOSTERSIDE
        );

        public final ResourceLocation engineblock;
        public final ResourceLocation bottomstage;
        public final ResourceLocation topstage;
        public final ResourceLocation payload;
        public final ResourceLocation payloadblocks;
        public final ResourceLocation les;
        public final ResourceLocation lesthrusters;
        public final ResourceLocation mainengines;
        public final ResourceLocation sideengines;
        public final ResourceLocation booster;
        public final ResourceLocation boosterside;

        SoyuzSkin(
                ResourceLocation engineblock,
                ResourceLocation bottomstage,
                ResourceLocation topstage,
                ResourceLocation payload,
                ResourceLocation payloadblocks,
                ResourceLocation les,
                ResourceLocation lesthrusters,
                ResourceLocation mainengines,
                ResourceLocation sideengines,
                ResourceLocation booster,
                ResourceLocation boosterside
        ) {
            this.engineblock = engineblock;
            this.bottomstage = bottomstage;
            this.topstage = topstage;
            this.payload = payload;
            this.payloadblocks = payloadblocks;
            this.les = les;
            this.lesthrusters = lesthrusters;
            this.mainengines = mainengines;
            this.sideengines = sideengines;
            this.booster = booster;
            this.boosterside = boosterside;
        }
    }

    public static void prontSoyuz(int type) {

        if(type >= SoyuzSkin.values().length || type < 0) return;

        prontMain(type);
        prontBoosters(type);
    }

    public static void prontMain(int type) {
        SoyuzSkin skin = SoyuzSkin.values()[type];

        RenderContext.pushPose();

        RenderSystem.setShaderTexture(0, skin.engineblock);
        ResourceManager.soyuz.renderOnly("EngineBlock");

        RenderSystem.setShaderTexture(0, skin.bottomstage);
        ResourceManager.soyuz.renderOnly("BottomStage");

        RenderSystem.setShaderTexture(0, skin.topstage);
        ResourceManager.soyuz.renderOnly("TopStage");

        RenderSystem.setShaderTexture(0, skin.payload);
        ResourceManager.soyuz.renderOnly("Payload");

        RenderSystem.setShaderTexture(0, ResourceManager.SOYUZ_MEMENTO);
        ResourceManager.soyuz.renderOnly("Memento");

        RenderSystem.setShaderTexture(0, skin.payloadblocks);
        ResourceManager.soyuz.renderOnly("PayloadBlocks");

        RenderSystem.setShaderTexture(0, skin.les);
        ResourceManager.soyuz.renderOnly("LES");

        RenderSystem.setShaderTexture(0, skin.lesthrusters);
        ResourceManager.soyuz.renderOnly("LESThrusters");

        RenderSystem.setShaderTexture(0, skin.mainengines);
        ResourceManager.soyuz.renderOnly("MainEngines");

        RenderSystem.setShaderTexture(0, skin.sideengines);
        ResourceManager.soyuz.renderOnly("SideEngines");

        RenderContext.popPose();
    }

    public static void prontBoosters(int type) {

        SoyuzSkin skin = SoyuzSkin.values()[type];

        RenderContext.pushPose();

        RenderSystem.setShaderTexture(0, skin.booster);
        ResourceManager.soyuz.renderOnly("Booster.000");
        ResourceManager.soyuz.renderOnly("Booster.001");
        ResourceManager.soyuz.renderOnly("Booster.002");
        ResourceManager.soyuz.renderOnly("Booster.003");

        RenderSystem.setShaderTexture(0, skin.mainengines);
        ResourceManager.soyuz.renderOnly("BoosterEngines.000");
        ResourceManager.soyuz.renderOnly("BoosterEngines.001");
        ResourceManager.soyuz.renderOnly("BoosterEngines.002");
        ResourceManager.soyuz.renderOnly("BoosterEngines.003");

        RenderSystem.setShaderTexture(0, skin.boosterside);
        ResourceManager.soyuz.renderOnly("BoosterSide.000");
        ResourceManager.soyuz.renderOnly("BoosterSide.001");
        ResourceManager.soyuz.renderOnly("BoosterSide.002");
        ResourceManager.soyuz.renderOnly("BoosterSide.003");

        RenderContext.popPose();
    }

    public static void prontCapsule() {

        RenderContext.pushPose();

        RenderSystem.setShaderTexture(0, ResourceManager.SOYUZ_MODULE_DOME_TEX);
        ResourceManager.soyuz_module.renderPart("Dome");
        RenderSystem.setShaderTexture(0, ResourceManager.SOYUZ_MODULE_LANDER_TEX);
        ResourceManager.soyuz_module.renderPart("Capsule");
        RenderSystem.setShaderTexture(0, ResourceManager.SOYUZ_MODULE_PROPULSION_TEX);
        ResourceManager.soyuz_module.renderPart("Propulsion");
        RenderSystem.setShaderTexture(0, ResourceManager.SOYUZ_MODULE_SOLAR_TEX);
        ResourceManager.soyuz_module.renderPart("Solar");

        RenderContext.popPose();

    }
}
