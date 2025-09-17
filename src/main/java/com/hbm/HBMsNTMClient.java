package com.hbm;

import com.hbm.entity.ModEntities;
import com.hbm.entity.mob.rendrer.EntityDuckRenderer;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.ModItems;
import com.hbm.packets.PacketsDispatcher;
import com.hbm.particle.*;
import com.hbm.util.i18n.I18nClient;
import com.hbm.util.i18n.ITranslate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.Random;

@Mod(value = HBMsNTM.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HBMsNTMClient {

    private static Random rand = new Random();

    private static final I18nClient I18N = new I18nClient();
    public ITranslate getI18n() { return I18N; }

    public HBMsNTMClient(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ModParticles.register(modBus);
        modBus.addListener(GeigerGUI::RegisterGuiLayers);
        modBus.addListener(this::registerParticles);
        modBus.addListener(PacketsDispatcher::registerPackets);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DUCK.get(), EntityDuckRenderer::new);
        ItemProperties.register(ModItems.POLAROID.get(),
                ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "polaroid_id"),
                (stack, level, entity, seed) -> CommonEvents.polaroidID);
    }
    @SubscribeEvent
    public static void drawTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> list = event.getToolTip();

        HazardSystem.addFullTooltip(stack, list);
    }
    private void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.SOME_PART.get(), ParticleSomePart.Provider::new);
        event.registerSpriteSet(ModParticles.MUKE_CLOUD.get(), ParticleMukeCloud.Provider::new);
        event.registerSpriteSet(ModParticles.MUKE_CLOUD_BF.get(), ParticleMukeCloud.Provider::new);
        event.registerSpriteSet(ModParticles.EXPLOSION_SMALL.get(), ParticleExplosionSmall.Provider::new);
        event.registerSpriteSet(ModParticles.MUKE_WAVE.get(), ParticleMukeWave.Provider::new);
        event.registerSpriteSet(ModParticles.COOLING_TOWER.get(), sprites -> {
            ModParticles.COOLING_TOWER_SPRITES = sprites;
            return new ParticleCoolingTower.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.MUKE_FLASH.get(), sprites -> {
            ModParticles.MUKE_FLASH_SPRITES = sprites;
            return new ParticleMukeFlash.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.GAS_FLAME.get(), ParticleGasFlame.Provider::new);
        event.registerSpriteSet(ModParticles.DEAD_LEAF.get(), ParticleDeadLeaf.Provider::new);
    }


    /**
     * Will get data and create particle from it.
     * For dimension, range and other things u will need packets.
     * @param data requires type, x, y, z to work
     */
    public static void effectNT(CompoundTag data) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        if (level == null) return;

        LocalPlayer player = mc.player;
        int particleSetting = mc.options.particles().get().getId();

        String type = data.getString("type");
        double x = data.getDouble("posX");
        double y = data.getDouble("posY");
        double z = data.getDouble("posZ");

        // for future
//        if (ParticleCreators.particleCreators.containsKey(type)) {
//            ParticleCreators.particleCreators.get(type).makeParticle(world, player, man, rand, x, y, z, data);
//            return;
//        }

        assert player != null; //shut up
        if ("muke".contains(type)) {
            ParticleMukeFlash fx = new ParticleMukeFlash(
                    level,
                    x, y, z,
                    data.getBoolean("balefire"),
                    ModParticles.MUKE_FLASH_SPRITES
            );
            level.addParticle(ModParticles.MUKE_WAVE.get(), x, y, z, 0.0, 0.0, 0.0);

            mc.particleEngine.add(fx);

            player.hurtTime = 15;
            player.hurtDuration = 15;
        }

        if ("tower".equals(type)) {
            if (particleSetting == 0 || (particleSetting == 1 && rand.nextBoolean())) {
                float strafe = 0.075F;
                boolean windDir  = true;
                float alphaMod = 0.25F;

                float lift = data.getFloat("lift");
                float maxScale = data.getFloat("max");
                float baseScale = data.getFloat("base");
                int lifetime = data.getInt("life");

                if (data.contains("noWind")) windDir  = !data.getBoolean("noWind");
                if (data.contains("strafe")) strafe = data.getFloat("strafe");
                if (data.contains("alpha")) alphaMod = data.getFloat("alpha");

                ParticleCoolingTower fx = new ParticleCoolingTower(
                        level,
                        x, y, z,
                        baseScale,
                        maxScale,
                        lift,
                        strafe,
                        windDir,
                        alphaMod,
                        lifetime,
                        ModParticles.COOLING_TOWER_SPRITES
                );

                if (data.contains("color")) {
                    int color = data.getInt("color");
                    fx.setColor(
                            (color >> 16 & 255) / 255F,
                            (color >> 8 & 255) / 255F,
                            (color & 255) / 255F
                    );
                }

                mc.particleEngine.add(fx);
            }
        }
    }
}
