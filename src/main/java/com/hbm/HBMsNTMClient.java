package com.hbm;

import com.hbm.entity.ModEntities;
import com.hbm.render.entity.effect.RenderTorex;
import com.hbm.handler.gui.GeigerGUI;
import com.hbm.hazard.HazardSystem;
import com.hbm.items.ModItems;
import com.hbm.packets.PacketsDispatcher;
import com.hbm.particle.*;
import com.hbm.render.EmptyRenderer;
import com.hbm.render.entity.mob.EntityDuckRenderer;
import com.hbm.util.i18n.I18nClient;
import com.hbm.util.i18n.ITranslate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.Random;

@Mod(value = HBMsNTM.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class HBMsNTMClient {
    private static final I18nClient I18N = new I18nClient();
    public ITranslate getI18n() { return I18N; }

    public HBMsNTMClient(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ModParticles.register(modBus);
        modBus.addListener(GeigerGUI::RegisterGuiLayers);
        modBus.addListener(this::registerParticles);
        modBus.addListener(PacketsDispatcher::registerPackets);
    }

    public static final int flashDuration = 5_000;
    public static long flashTimestamp;
    public static final int shakeDuration = 1_500;
    public static long shakeTimestamp;

    public static boolean enableFlash = true;
    public static boolean enableShake = true;

    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        if (!enableShake) return;

        long now = System.currentTimeMillis();
        long end = shakeTimestamp + shakeDuration;

        if (now < end) {
            float mult = (end - now) / (float) shakeDuration * 2.0F;
            double horizontal = Mth.clamp(Math.sin(now * 0.02), -0.7, 0.7) * 15;
            double vertical   = Mth.clamp(Math.sin(now * 0.01 + 2), -0.7, 0.7) * 3;

            GuiGraphics graphics = event.getGuiGraphics();
            graphics.pose().translate(horizontal * mult, vertical * mult, 0);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPost(RenderGuiEvent.Post event) {
        if (!enableFlash) return;

        long now = System.currentTimeMillis();
        long end = flashTimestamp + flashDuration;

        if (now < end) {
            float brightness = (end - now) / (float) flashDuration;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            guiGraphics.fill(0, 0, width, height,
                    FastColor.ARGB32.color((int)(brightness * 255), 255, 255, 255));
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DUCK.get(), EntityDuckRenderer::new);
        event.registerEntityRenderer(ModEntities.NUKE_MK5.get(), EmptyRenderer::new);
        event.registerEntityRenderer(ModEntities.NUKE_TOREX.get(), RenderTorex::new);
        event.registerEntityRenderer(ModEntities.FALLOUT_RAIN.get(), EmptyRenderer::new);
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
        event.registerSpriteSet(ModParticles.AURA.get(), sprites -> {
            ModParticles.AURA_SPITES = sprites;
            return new ParticleAura.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.VOMIT.get(), sprites -> {
            ModParticles.VOMIT_SPRITES = sprites;
            return new VomitPart.Provider(sprites);
        });
        event.registerSpriteSet(ModParticles.RAD_FOG.get(), sprites -> {
            ModParticles.RAD_FOG_SPRITES = sprites;
            return new VomitPart.Provider(sprites);
        });
    }

    public static void effectNT(CompoundTag data) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;

        if (level == null) return;

        Player player = mc.player;
        int particleSetting = mc.options.particles().get().getId();

        String type = data.getString("type");
        double x = data.getDouble("posX");
        double y = data.getDouble("posY");
        double z = data.getDouble("posZ");

        Random rand = new Random();

        if("radFog".equals(type)) {
            ParticleRadiationFog fx = new ParticleRadiationFog(level, x, y, z, 0.62F, 0.67F, 0.38F, 5F, ModParticles.RAD_FOG_SPRITES);
            mc.particleEngine.add(fx);
        }
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
        if ("radiation".equals(type)) {
            if (player == null) return;
            for (int i = 0; i < data.getInt("count"); i++) {
                ParticleAura fx = new ParticleAura(
                        level,
                        player.getX() + rand.nextGaussian() * 4,
                        player.getY() + rand.nextGaussian() * 2,
                        player.getZ() + rand.nextGaussian() * 4,
                        rand.nextGaussian(),
                        rand.nextGaussian(),
                        rand.nextGaussian(),
                        0F,
                        0.75F,
                        1F,
                        ModParticles.AURA_SPITES
                );

                mc.particleEngine.add(fx);
            }
        }
        if ("vomit".equals(type)) {
            Entity e = level.getEntity(data.getInt("entity"));
            int count = data.getInt("count") / (particleSetting + 1);

            if (e instanceof LivingEntity living) {
                double ix = living.getX();
                double iy = living.getY() + living.getEyeHeight();
                double iz = living.getZ();

                Vec3 vec = living.getLookAngle();

                for (int i = 0; i < count; i++) {
                    String mode = data.getString("mode");

                    if ("normal".equals(mode)) {
                        VomitPart fx = new VomitPart(level, ix, iy, iz,
                                (vec.x + rand.nextGaussian() * 0.2) * 0.2,
                                (vec.y + rand.nextGaussian() * 0.2) * 0.2,
                                (vec.z + rand.nextGaussian() * 0.2) * 0.2,
                                0.3F, 0.33F + rand.nextFloat(0.2F), 0.17F,
                                ModParticles.VOMIT_SPRITES
                        );
                        mc.particleEngine.add(fx);
                    }

                    if ("blood".equals(mode)) {
                        VomitPart fx = new VomitPart(level, ix, iy, iz,
                                (vec.x + rand.nextGaussian() * 0.2) * 0.2,
                                (vec.y + rand.nextGaussian() * 0.2) * 0.2,
                                (vec.z + rand.nextGaussian() * 0.2) * 0.2,
                                0.72F + rand.nextFloat(0.3F), 0.12F, 0F,
                                ModParticles.VOMIT_SPRITES
                        );
                        mc.particleEngine.add(fx);
                    }

                    if ("smoke".equals(mode)) {

                        level.addParticle(
                                ParticleTypes.SMOKE,
                                ix, iy, iz,
                                (vec.x + rand.nextGaussian() * 0.1) * 0.05,
                                (vec.y + rand.nextGaussian() * 0.1) * 0.05,
                                (vec.z + rand.nextGaussian() * 0.1) * 0.05
                        );
                    }
                }
            }
        }
    }
}
