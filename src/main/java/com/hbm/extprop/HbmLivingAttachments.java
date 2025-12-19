package com.hbm.extprop;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.entity.mob.Duck;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModDamageSource;
import com.hbm.network.toclient.InformPlayer;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class HbmLivingAttachments {
    private static final ResourceLocation DIGAMMA_MOD = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "digamma");

    public static final StreamCodec<RegistryFriendlyByteBuf, HbmLivingAttachments> STREAM_CODEC = StreamCodec.of(
            (buf, props) -> buf.writeNbt(props.saveNBTData()),
            buf -> {
                HbmLivingAttachments props = new HbmLivingAttachments();
                props.loadNBTData(buf.readNbt());
                return props;
            });
    public static final Codec<HbmLivingAttachments> CODEC =
            CompoundTag.CODEC.xmap(
                    tag -> {
                        HbmLivingAttachments props = new HbmLivingAttachments();
                        props.loadNBTData(tag);
                        return props;
                        },
                    HbmLivingAttachments::saveNBTData
            );


    private float radiation;
    private float digamma;
    private int asbestos;
    public static final int maxAsbestos = 60 * 60 * 20;
    private int blacklung;
    public static final int maxBlacklung = 2 * 60 * 60 * 20;
    private float radEnv;
    private float radBuf;
    private int bombTimer;
    private int contagion;
    private int oil;
    public int fire;
    public int phosphorus;
    public int balefire;
    public int blackFire;
    private final List<ContaminationEffect> CONTAMINATION = new ArrayList<>();

    public HbmLivingAttachments() { }

    public static HbmLivingAttachments getData(LivingEntity entity) {
        return entity.getData(ModAttachments.LIVING_ATTACHMENT);
    }

    /// RADIATION ///
    public static float getRadiation(LivingEntity entity) {
        if (!MainConfig.COMMON.ENABLE_CONTAMINATION.get()) return 0;
        return getData(entity).radiation;
    }

    public static void setRadiation(LivingEntity entity, float rad) {
        if (MainConfig.COMMON.ENABLE_CONTAMINATION.get()) {
            HbmLivingAttachments props = getData(entity);
            props.radiation = rad;
            entity.setData(ModAttachments.LIVING_ATTACHMENT, props);
        }
    }

    public static void incrementRadiation(LivingEntity entity, float rad) {
        if (!MainConfig.COMMON.ENABLE_CONTAMINATION.get()) return;
        float radiation = getData(entity).radiation + rad;

        if (radiation > 2500) radiation = 2500;
        if (radiation < 0) radiation = 0;

        setRadiation(entity, radiation);
    }

    /// RAD ENV ///
    public static float getRadEnv(LivingEntity entity) { return getData(entity).radEnv; }
    public static void setRadEnv(LivingEntity entity, float rad) { getData(entity).radEnv = rad; }

    /// RAD BUF ///
    public static float getRadBuf(LivingEntity entity) { return getData(entity).radBuf; }
    public static void setRadBuf(LivingEntity entity, float rad) { getData(entity).radBuf = rad; }

    /// CONTAMINATION ///
    public static List<ContaminationEffect> getCont(LivingEntity entity) { return getData(entity).CONTAMINATION; }
    public static void addCont(LivingEntity entity, ContaminationEffect cont) { getData(entity).CONTAMINATION.add(cont); }

    /// DIGAMMA ///
    public static float getDigamma(LivingEntity entity) {
        return getData(entity).digamma;
    }

    public static void setDigamma(LivingEntity entity, float digamma) {
        if (entity.level().isClientSide)
            return;

        if (entity instanceof Duck)
            digamma = 0.0F;

        HbmLivingAttachments props = getData(entity);
        props.digamma = digamma;
        entity.setData(ModAttachments.LIVING_ATTACHMENT, props);

        float healthMod = (float) Math.pow(0.5, digamma) - 1F;

        AttributeInstance attributeInstance = entity.getAttribute(Attributes.MAX_HEALTH);

        if (attributeInstance != null) {
            attributeInstance.removeModifier(DIGAMMA_MOD);

            AttributeModifier modifier = new AttributeModifier(DIGAMMA_MOD, healthMod, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

            attributeInstance.addPermanentModifier(modifier);

            if (entity.getHealth() > entity.getMaxHealth() && entity.getMaxHealth() > 0) {
                entity.setHealth(entity.getMaxHealth());
            }
        }
        if ((entity.getMaxHealth() <= 0 || digamma >= 10.0F) && entity.isAlive()) {
            entity.hurt(entity.damageSources().source(ModDamageSource.DIGAMMA), Float.MAX_VALUE);
        }
    }

    public static void incrementDigamma(LivingEntity entity, float digamma) {
        if (entity instanceof Duck)
            digamma = 0.0F;

        float dRad = getDigamma(entity) + digamma;

        if (dRad > 10) dRad = 10;
        if (dRad < 0) dRad = 0;

        setDigamma(entity, dRad);
    }

    /// ASBESTOS ///
    public static int getAsbestos(LivingEntity entity) {
        if (MainConfig.COMMON.DISABLE_ASBESTOS.get()) return 0;
        return getData(entity).asbestos;
    }

    public static void setAsbestos(LivingEntity entity, int asbestos) {
        if (MainConfig.COMMON.DISABLE_ASBESTOS.get()) return;
        HbmLivingAttachments props = getData(entity);
        props.asbestos = asbestos;
        entity.setData(ModAttachments.LIVING_ATTACHMENT, props);

        if (asbestos >= maxAsbestos) {
            getData(entity).asbestos = 0;
            entity.hurt(entity.damageSources().source(ModDamageSource.ASBESTOS), Float.MAX_VALUE);
        }
    }

    public static void incrementAsbestos(LivingEntity entity, int asbestos) {
        if (MainConfig.COMMON.DISABLE_ASBESTOS.get()) return;
        setAsbestos(entity, getAsbestos(entity) + asbestos);

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new InformPlayer(Component.translatable("info.asbestos").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_GAS_HAZARD, 3000));
        }
    }

    /// BLACK LUNG DISEASE ///
    public static int getBlackLung(LivingEntity entity) {
        if (MainConfig.COMMON.DISABLE_COAL.get()) return 0;
        return getData(entity).blacklung;
    }

    public static void setBlackLung(LivingEntity entity, int blacklung) {
        if (MainConfig.COMMON.DISABLE_COAL.get()) return;
        HbmLivingAttachments props = getData(entity);
        props.blacklung = blacklung;
        entity.setData(ModAttachments.LIVING_ATTACHMENT, props);

        if (blacklung >= maxBlacklung) {
            getData(entity).blacklung = 0;
            entity.hurt(entity.damageSources().source(ModDamageSource.BLACKLUNG), Float.MAX_VALUE);
        }
    }

    public static void incrementBlackLung(LivingEntity entity, int blacklung) {
        if (MainConfig.COMMON.DISABLE_COAL.get()) return;
        setBlackLung(entity, getBlackLung(entity) + blacklung);

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new InformPlayer(Component.translatable("info.coaldust").withStyle(ChatFormatting.RED), HBMsNTMClient.ID_GAS_HAZARD, 3000));
        }
    }

    /// TIME BOMB ///
    public static int getTimer(LivingEntity entity) {
        return getData(entity).bombTimer;
    }

    public static void setTimer(LivingEntity entity, int bombTimer) {
        getData(entity).bombTimer = bombTimer;
    }

    /// CONTAGION ///
    public static int getContagion(LivingEntity entity) {
        return getData(entity).contagion;
    }

    public static void setContagion(LivingEntity entity, int contagion) {
        getData(entity).contagion = contagion;
    }

    /// OIL ///
    public static int getOil(LivingEntity entity) {
        return getData(entity).oil;
    }

    public static void setOil(LivingEntity entity, int oil) {
        getData(entity).oil = oil;
    }

    public CompoundTag saveNBTData() {
        CompoundTag props = new CompoundTag();

        props.putFloat("hfr_radiation", radiation);
        props.putFloat("hfr_digamma", digamma);
        props.putInt("hfr_asbestos", asbestos);
        props.putInt("hfr_bomb", bombTimer);
        props.putInt("hfr_contagion", contagion);
        props.putInt("hfr_blacklung", blacklung);
        props.putInt("hfr_oil", oil);
        props.putInt("hfr_fire", fire);
        props.putInt("hfr_phosphorus", phosphorus);
        props.putInt("hfr_balefire", balefire);
        props.putInt("hfr_blackfire", blackFire);

        props.putInt("hfr_cont_count", this.CONTAMINATION.size());

        for (int i = 0; i < this.CONTAMINATION.size(); i++) {
            this.CONTAMINATION.get(i).save(props, i);
        }

        return props;
    }

    public void loadNBTData(CompoundTag props) {
        if (props != null) {
            radiation = props.getFloat("hfr_radiation");
            digamma = props.getFloat("hfr_digamma");
            asbestos = props.getInt("hfr_asbestos");
            bombTimer = props.getInt("hfr_bomb");
            contagion = props.getInt("hfr_contagion");
            blacklung = props.getInt("hfr_blacklung");
            oil = props.getInt("hfr_oil");
            fire = props.getInt("hfr_fire");
            phosphorus = props.getInt("hfr_phosphorus");
            balefire = props.getInt("hfr_balefire");
            blackFire = props.getInt("hfr_blackfire");

            int cont = props.getInt("hfr_cont_count");

            this.CONTAMINATION.clear();
            for (int i = 0; i < cont; i++) {
                this.CONTAMINATION.add(ContaminationEffect.load(props, i));
            }
        }
    }

    public static class ContaminationEffect {
        public float maxRad;
        public int maxTime;
        public int time;
        public boolean ignoreArmor;

        public ContaminationEffect(float rad, int time, boolean ignoreArmor) {
            this.maxRad = rad;
            this.maxTime = this.time = time;
            this.ignoreArmor = ignoreArmor;
        }

        public float getRad() {
            return maxRad * ((float) time / (float) maxTime);
        }

        public void save(CompoundTag tag, int index) {
            CompoundTag me = new CompoundTag();
            me.putFloat("maxRad", this.maxRad);
            me.putInt("maxTime", this.maxTime);
            me.putInt("time", this.time);
            tag.putBoolean("ignoreArmor", ignoreArmor);
            tag.put("cont_" + index, me);
        }

        public static ContaminationEffect load(CompoundTag tag, int index) {
            CompoundTag me = (CompoundTag) tag.get("cont_" + index);
            float maxRad = me.getFloat("maxRad");
            int maxTime = me.getInt("maxTime");
            int time = me.getInt("time");
            boolean ignoreArmor = me.getBoolean("ignoreArmor");

            ContaminationEffect effect = new ContaminationEffect(maxRad, maxTime, ignoreArmor);
            effect.time = time;
            return effect;
        }
    }
}
