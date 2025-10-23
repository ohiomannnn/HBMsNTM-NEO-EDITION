package com.hbm.extprop;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.ModConfigs;
import com.hbm.entity.mob.Duck;
import com.hbm.lib.ModAttachments;
import com.hbm.lib.ModDamageSource;
import com.hbm.network.toclient.InformPlayerPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class LivingProperties {
    public LivingEntity entity;
    private static final ResourceLocation DIGAMMA_MOD = ResourceLocation.fromNamespaceAndPath(HBMsNTM.MODID, "digamma");

    /// VALS ///
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
    private final List<ContaminationEffect> contamination = new ArrayList<>();

    public LivingProperties(IAttachmentHolder iAttachmentHolder) {
        if (iAttachmentHolder instanceof LivingEntity livingEntity) {
            this.entity = livingEntity;
        }
    }

    public static LivingProperties getData(LivingEntity entity) {
        return entity.getData(ModAttachments.LIVING_PROPS);
    }

    /// RADIATION ///
    public static float getRadiation(LivingEntity entity) {
        if (!ModConfigs.COMMON.ENABLE_CONTAMINATION.get()) return 0;
        return getData(entity).radiation;
    }

    public static void setRadiation(LivingEntity entity, float rad) {
        if (ModConfigs.COMMON.ENABLE_CONTAMINATION.get())
            getData(entity).radiation = rad;
    }

    public static void incrementRadiation(LivingEntity entity, float rad) {
        if (!ModConfigs.COMMON.ENABLE_CONTAMINATION.get()) return;
        float radiation = getData(entity).radiation + rad;

        if (radiation > 2500)
            radiation = 2500;
        if (radiation < 0)
            radiation = 0;

        setRadiation(entity, radiation);
    }

    /// RAD ENV ///
    public static float getRadEnv(LivingEntity entity) {
        return getData(entity).radEnv;
    }

    public static void setRadEnv(LivingEntity entity, float rad) {
        getData(entity).radEnv = rad;
    }

    /// RAD BUF ///
    public static float getRadBuf(LivingEntity entity) {
        return getData(entity).radBuf;
    }

    public static void setRadBuf(LivingEntity entity, float rad) {
        getData(entity).radBuf = rad;
    }

    /// CONTAMINATION ///
    public static List<ContaminationEffect> getCont(LivingEntity entity) {
        return getData(entity).contamination;
    }

    public static void addCont(LivingEntity entity, ContaminationEffect cont) {
        getData(entity).contamination.add(cont);
    }

    /// DIGAMMA ///
    public static float getDigamma(LivingEntity entity) {
        return getData(entity).digamma;
    }

    public static void setDigamma(LivingEntity entity, float digamma) {

        if (entity.level().isClientSide)
            return;

        if (entity instanceof Duck)
            digamma = 0.0F;

        getData(entity).digamma = digamma;

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
            DamageSource src = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.DIGAMMA));
            entity.hurt(src, Float.MAX_VALUE);
        }
    }

    public static void incrementDigamma(LivingEntity entity, float digamma) {
        if (entity instanceof Duck)
            digamma = 0.0F;

        float dRad = getDigamma(entity) + digamma;

        if (dRad > 10)
            dRad = 10;
        if (dRad < 0)
            dRad = 0;

        setDigamma(entity, dRad);
    }

    /// ASBESTOS ///
    public static int getAsbestos(LivingEntity entity) {
        if (ModConfigs.COMMON.DISABLE_ASBESTOS.get()) return 0;
        return getData(entity).asbestos;
    }

    public static void setAsbestos(LivingEntity entity, int asbestos) {
        if (ModConfigs.COMMON.DISABLE_ASBESTOS.get()) return;
        getData(entity).asbestos = asbestos;

        if (asbestos >= maxAsbestos) {
            getData(entity).asbestos = 0;
            DamageSource src = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.ASBESTOS));
            entity.hurt(src, Float.MAX_VALUE);
        }
    }

    public static void incrementAsbestos(LivingEntity entity, int asbestos) {
        if (ModConfigs.COMMON.DISABLE_ASBESTOS.get()) return;
        setAsbestos(entity, getAsbestos(entity) + asbestos);

        if (entity instanceof ServerPlayer player) {
            Component comp = Component.translatable("info.asbestos").withStyle(ChatFormatting.RED);
            InformPlayerPacket packet = new InformPlayerPacket(true, null, comp, HBMsNTMClient.ID_GAS_HAZARD, 3000);
            PacketDistributor.sendToPlayer(player, packet);
        }
    }

    /// BLACK LUNG DISEASE ///
    public static int getBlackLung(LivingEntity entity) {
        if (ModConfigs.COMMON.DISABLE_COAL.get()) return 0;
        return getData(entity).blacklung;
    }

    public static void setBlackLung(LivingEntity entity, int blacklung) {
        if (ModConfigs.COMMON.DISABLE_COAL.get()) return;
        getData(entity).blacklung = blacklung;

        if (blacklung >= maxBlacklung) {
            getData(entity).blacklung = 0;
            DamageSource src = new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageSource.BLACKLUNG));
            entity.hurt(src, Float.MAX_VALUE);
        }
    }

    public static void incrementBlackLung(LivingEntity entity, int blacklung) {
        if (ModConfigs.COMMON.DISABLE_COAL.get()) return;
        setBlackLung(entity, getBlackLung(entity) + blacklung);

        if (entity instanceof ServerPlayer player) {
            Component comp = Component.translatable("info.coaldust").withStyle(ChatFormatting.RED);
            InformPlayerPacket packet = new InformPlayerPacket(true, null, comp, HBMsNTMClient.ID_GAS_HAZARD, 3000);
            PacketDistributor.sendToPlayer(player, packet);
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

    private static boolean isCreative(LivingEntity entity) {
        if (!(entity instanceof Player player)) return false;
        if (player.isSpectator()) return true;
        return player.isCreative();
    }

    /// ---- Serializing ---- ///
    public CompoundTag serializeNBT() {
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

        props.putInt("hfr_cont_count", this.contamination.size());

        for (int i = 0; i < this.contamination.size(); i++) {
            this.contamination.get(i).save(props, i);
        }

        return props;
    }

    public void deserializeNBT(CompoundTag props) {
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

            this.contamination.clear();
            for (int i = 0; i < cont; i++) {
                this.contamination.add(ContaminationEffect.load(props, i));
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

        public void save(CompoundTag nbt, int index) {
            CompoundTag me = new CompoundTag();
            me.putFloat("maxRad", this.maxRad);
            me.putInt("maxTime", this.maxTime);
            me.putInt("time", this.time);
            me.putBoolean("ignoreArmor", ignoreArmor);
            nbt.put("cont_" + index, me);
        }

        public static ContaminationEffect load(CompoundTag nbt, int index) {
            CompoundTag me = (CompoundTag) nbt.get("cont_" + index);
            assert me != null;
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
