package com.hbm.items.weapon.sedna;

import com.hbm.handler.HbmKeybinds.EnumKeybind;
import com.hbm.inventory.RecipesCommon.ComparableStack;
import com.hbm.items.IEquipReceiver;
import com.hbm.items.IHUDItem;
import com.hbm.items.IKeybindReceiver;
import com.hbm.items.weapon.sedna.factory.GunFactory.Ammo;
import com.hbm.items.weapon.sedna.mods.XWeaponModManager;
import com.hbm.main.NuclearTechMod;
import com.hbm.network.toclient.HbmAnimation;
import com.hbm.render.anim.AnimationEnums.GunAnimation;
import com.hbm.sound.AudioWrapper;
import com.hbm.util.EnumUtil;
import com.hbm.util.TagsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent.Pre;
import net.neoforged.neoforge.network.PacketDistributor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GunBaseNTItem extends Item implements IKeybindReceiver, IHUDItem, IEquipReceiver {

    /** Timestamp for rendering smoke nodes and muzzle flashes */
    public long[] lastShot;
    /** [0;1] randomized every shot for various rendering applications */
    public double shotRand = 0D;

    public static List<Item> secrets = new ArrayList<>();
    public List<ComparableStack> recognizedMods = new ArrayList<>();

    public ItemStack defaultAmmo;
    public boolean isDefaultExpensive = false;

    public static final DecimalFormatSymbols SYMBOLS_US = new DecimalFormatSymbols(Locale.US);
    public static final DecimalFormat FORMAT_DMG = new DecimalFormat("#.##", SYMBOLS_US);

    public static float recoilVertical = 0;
    public static float recoilHorizontal = 0;
    public static float recoilDecay = 0.75F;
    public static float recoilRebound = 0.25F;
    public static float offsetVertical = 0;
    public static float offsetHorizontal = 0;

    public static void setupRecoil(float vertical, float horizontal, float decay, float rebound) {
        recoilVertical += vertical;
        recoilHorizontal += horizontal;
        recoilDecay = decay;
        recoilRebound = rebound;
    }

    public static void setupRecoil(float vertical, float horizontal) {
        setupRecoil(vertical, horizontal, 0.75F, 0.25F);
    }

    public static final String O_GUNCONFIG = "O_GUNCONFIG_";

    public static final String KEY_DRAWN = "drawn";
    public static final String KEY_AIMING = "aiming";
    public static final String KEY_MODE = "mode_";
    public static final String KEY_WEAR = "wear_";
    public static final String KEY_TIMER = "timer_";
    public static final String KEY_STATE = "state_";
    public static final String KEY_PRIMARY = "mouse1_";
    public static final String KEY_SECONDARY = "mouse2_";
    public static final String KEY_TERTIARY = "mouse3_";
    public static final String KEY_RELOAD = "reload_";
    public static final String KEY_LASTANIM = "lastanim_";
    public static final String KEY_ANIMTIMER = "animtimer_";
    public static final String KEY_LOCKONTARGET = "lockontarget";
    public static final String KEY_LOCKEDON = "lockedon";
    public static final String KEY_CANCELRELOAD = "cancel";
    public static final String KEY_EQUIPPED = "eqipped";

    public static ConcurrentHashMap<LivingEntity, AudioWrapper> loopedSounds = new ConcurrentHashMap<>();

    public static float prevAimingProgress;
    public static float aimingProgress;

    /** NEVER ACCESS DIRECTLY - USE GETTER */
    protected GunConfig[] configs_DNA;

    public Function<ItemStack, Component> LAMBDA_NAME_MUTATOR;
    public WeaponQuality quality;

    public GunConfig getConfig(ItemStack stack, int index) {
        GunConfig cfg = configs_DNA[index];
        if(stack == null) return cfg;
        return XWeaponModManager.eval(cfg, stack, O_GUNCONFIG + index, this, index);
    }

    public int getConfigCount() {
        return configs_DNA.length;
    }

    public GunBaseNTItem(WeaponQuality quality, GunConfig... cfg) {
        super(new Properties().stacksTo(1));
        this.configs_DNA = cfg;
        this.quality = quality;
        this.lastShot = new long[cfg.length];
        for(int i = 0; i < cfg.length; i++) cfg[i].index = i;
        //if(quality == WeaponQuality.A_SIDE || quality == WeaponQuality.SPECIAL || quality == WeaponQuality.UTILITY) this.setCreativeTab(MainRegistry.weaponTab);
        //if(quality == WeaponQuality.LEGENDARY || quality == WeaponQuality.SECRET) this.secrets.add(this);
        //this.setTextureName(RefStrings.MODID + ":gun_darter");
    }

    public enum WeaponQuality {
        A_SIDE,
        B_SIDE,
        LEGENDARY,
        SPECIAL,
        UTILITY,
        SECRET,
        DEBUG
    }

    public enum GunState {
        DRAWING,	//forced delay where nothing can be done
        IDLE,		//the gun is ready to fire or reload
        COOLDOWN,	//forced delay, but with option for refire
        RELOADING,	//forced delay after which a reload action happens, may be canceled (TBI)
        JAMMED,		//forced delay due to jamming
    }

    public GunBaseNTItem setDefaultAmmo(Ammo ammo, int amount) {
       // this.defaultAmmo = new ItemStack(ModItems.ammo_standard, amount, ammo.ordinal());
        return this;
    }

    public GunBaseNTItem setDefaultAmmoExpensive(Ammo ammo, int amount) {
        this.isDefaultExpensive = true;
        return setDefaultAmmo(ammo, amount);
    }

    public GunBaseNTItem setNameMutator(Function<ItemStack, Component> lambda) {
        this.LAMBDA_NAME_MUTATOR = lambda;
        return this;
    }

    @Override
    public Component getName(ItemStack stack) {

        if(this.LAMBDA_NAME_MUTATOR != null) {
            Component component = this.LAMBDA_NAME_MUTATOR.apply(stack);
            if(component != null) return component;
        }

        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {

    }

    @Override
    public boolean canHandleKeybind(Player player, ItemStack stack, EnumKeybind keybind) {
        return keybind == EnumKeybind.GUN_PRIMARY || keybind == EnumKeybind.GUN_SECONDARY || keybind == EnumKeybind.GUN_TERTIARY || keybind == EnumKeybind.RELOAD;
    }

    @Override
    public void handleKeybind(Player player, ItemStack stack, EnumKeybind keybind, boolean newState) {
        handleKeybind(player, player.inventory, stack, keybind, newState);
    }

    public void handleKeybind(LivingEntity entity, Container inventory, ItemStack stack, EnumKeybind keybind, boolean newState) {
        //if(!GeneralConfig.enableGuns) return;

        int configs = this.configs_DNA.length;

        for(int i = 0; i < configs; i++) {
            GunConfig config = getConfig(stack, i);
            LambdaContext ctx = new LambdaContext(config, entity, inventory, i);

            if(keybind == EnumKeybind.GUN_PRIMARY &&	newState && !getPrimary(stack, i)) {	if(config.getPressPrimary(stack) != null)		config.getPressPrimary(stack).accept(stack, ctx);		setPrimary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.GUN_PRIMARY &&	!newState && getPrimary(stack, i)) {	if(config.getReleasePrimary(stack) != null)		config.getReleasePrimary(stack).accept(stack, ctx);		setPrimary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.GUN_SECONDARY &&	newState && !getSecondary(stack, i)) {	if(config.getPressSecondary(stack) != null)		config.getPressSecondary(stack).accept(stack, ctx);		setSecondary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.GUN_SECONDARY &&	!newState && getSecondary(stack, i)) {	if(config.getReleaseSecondary(stack) != null)	config.getReleaseSecondary(stack).accept(stack, ctx);	setSecondary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.GUN_TERTIARY &&	newState && !getTertiary(stack, i)) {	if(config.getPressTertiary(stack) != null)		config.getPressTertiary(stack).accept(stack, ctx);		setTertiary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.GUN_TERTIARY &&	!newState && getTertiary(stack, i)) {	if(config.getReleaseTertiary(stack) != null)	config.getReleaseTertiary(stack).accept(stack, ctx);	setTertiary(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.RELOAD &&			newState && !getReloadKey(stack, i)) {	if(config.getPressReload(stack) != null)		config.getPressReload(stack).accept(stack, ctx);		setReloadKey(stack, i, newState);	continue; }
            if(keybind == EnumKeybind.RELOAD &&			!newState && getReloadKey(stack, i)) {	if(config.getReleaseReload(stack) != null)		config.getReleaseReload(stack).accept(stack, ctx);		setReloadKey(stack, i, newState);	continue; }
        }
    }

    @Override
    public void onEquip(Player player, ItemStack stack) {
        for(int i = 0; i < this.configs_DNA.length; i++) {
            if(getLastAnim(stack, i) == GunAnimation.EQUIP && getAnimTimer(stack, i) < 5) continue;
            playAnimation(player, stack, GunAnimation.EQUIP, i);
            setPrimary(stack, i, false);
            setSecondary(stack, i, false);
            setTertiary(stack, i, false);
            setReloadKey(stack, i, false);
        }
    }

    public static void playAnimation(Player player, ItemStack stack, GunAnimation type, int index) {
        if(player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new HbmAnimation((short) type.ordinal(), 0, index));
        }

        setLastAnim(stack, index, type);
        setAnimTimer(stack, index, 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {

        if(!(entity instanceof LivingEntity livingEntity)) return;
        Player player = entity instanceof Player instancePlayer ? instancePlayer : null;
        int confNo = this.configs_DNA.length;
        GunConfig[] configs = new GunConfig[confNo];
        LambdaContext[] ctx = new LambdaContext[confNo];
        for(int i = 0; i < confNo; i++) {
            configs[i] = this.getConfig(stack, i);
            ctx[i] = new LambdaContext(configs[i], livingEntity, player != null ? player.inventory : null, i);
        }

        if(level.isClientSide) {
            this.tickClient(stack, confNo, configs, ctx, isSelected, player);
            return;
        }

        /// ON EQUIP ///
        if(player != null) {
            boolean wasHeld = getIsEquipped(stack);

            if(!wasHeld && isSelected && player != null) {
                this.onEquip(player, stack);
            }
        }

        setIsEquipped(stack, isSelected);

        /// RESET WHEN NOT EQUIPPED ///
        if(!isSelected) {
            for(int i = 0; i < confNo; i++) {
                GunState current = getState(stack, i);
                if(current != GunState.JAMMED) {
                    setState(stack, i, GunState.DRAWING);
                    setTimer(stack, i, configs[i].getDrawDuration(stack));
                }
                setLastAnim(stack, i, GunAnimation.CYCLE); //prevents new guns from initializing with DRAWING, 0
            }
            setIsAiming(stack, false);
            setReloadCancel(stack, false);
            return;
        }

        for(int i = 0; i < confNo; i++) for(int k = 0; k == 0 || (k < 2 /*ArmorTrenchmaster.isTrenchMaster(player)*/ && getState(stack, i) == GunState.RELOADING); k++) {
            BiConsumer<ItemStack, LambdaContext> orchestra = configs[i].getOrchestra(stack);
            if(orchestra != null) orchestra.accept(stack, ctx[i]);

            setAnimTimer(stack, i, getAnimTimer(stack, i) + 1);

            /// STTATE MACHINE ///
            int timer = getTimer(stack, i);
            if(timer > 0) setTimer(stack, i, timer - 1);
            if(timer <= 1) configs[i].getDecider(stack).accept(stack, ctx[i]);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void tickClient(ItemStack stack, int confNo, GunConfig[] configs, LambdaContext[] ctx, boolean isSelected, Player player) {
        if(isSelected && player == Minecraft.getInstance().player) {

            /// DEBUG ///
			/*Vec3 offset = Vec3.createVectorHelper(-0.2, -0.1, 0.75);
			offset.rotateAroundX(-entity.rotationPitch / 180F * (float) Math.PI);
			offset.rotateAroundY(-entity.rotationYaw / 180F * (float) Math.PI);
			world.spawnParticle("flame", entity.posX + offset.xCoord, entity.posY + entity.getEyeHeight() + offset.yCoord, entity.posZ + offset.zCoord, 0, 0, 0);*/

            /// AIMING ///
            prevAimingProgress = aimingProgress;
            boolean aiming = getIsAiming(stack);
            float aimSpeed = 0.25F;
            if(aiming && aimingProgress < 1F) aimingProgress += aimSpeed;
            if(!aiming && aimingProgress > 0F) aimingProgress -= aimSpeed;
            aimingProgress = Math.clamp(aimingProgress, 0F, 1F);

            /// SMOKE NODES ///
            for(int i = 0; i < confNo; i++) if(configs[i].getSmokeHandler(stack) != null) {
                configs[i].getSmokeHandler(stack).accept(stack, ctx[i]);
            }

            for(int i = 0; i < confNo; i++) {
                BiConsumer<ItemStack, LambdaContext> orchestra = configs[i].getOrchestra(stack);
                if(orchestra != null) orchestra.accept(stack, ctx[i]);
            }
        }
    }

    // GUN DRAWN //
    public static boolean getIsDrawn(ItemStack stack) { return getValueBool(stack, KEY_DRAWN); }
    public static void setIsDrawn(ItemStack stack, boolean value) { setValueBool(stack, KEY_DRAWN, value); }
    // GUN STATE TIMER //
    public static int getTimer(ItemStack stack, int index) { return getValueInt(stack, KEY_TIMER + index); }
    public static void setTimer(ItemStack stack, int index, int value) { setValueInt(stack, KEY_TIMER + index, value); }
    // GUN STATE //
    public static GunState getState(ItemStack stack, int index) { return EnumUtil.grabEnumSafely(GunState.class, getValueByte(stack, KEY_STATE + index)); }
    public static void setState(ItemStack stack, int index, GunState value) { setValueByte(stack, KEY_STATE + index, (byte) value.ordinal()); }
    // GUN MODE //
    public static int getMode(ItemStack stack, int index) { return getValueInt(stack, KEY_MODE + index); }
    public static void setMode(ItemStack stack, int index, int value) { setValueInt(stack, KEY_MODE + index, value); }
    // GUN AIMING //
    public static boolean getIsAiming(ItemStack stack) { return getValueBool(stack, KEY_AIMING); }
    public static void setIsAiming(ItemStack stack, boolean value) { setValueBool(stack, KEY_AIMING, value); }
    // GUN AIMING //
    public static float getWear(ItemStack stack, int index) { return getValueFloat(stack, KEY_WEAR + index); }
    public static void setWear(ItemStack stack, int index, float value) { setValueFloat(stack, KEY_WEAR + index, value); }
    // LOCKON //
    public static int getLockonTarget(ItemStack stack) { return getValueInt(stack, KEY_LOCKONTARGET); }
    public static void setLockonTarget(ItemStack stack, int value) { setValueInt(stack, KEY_LOCKONTARGET, value); }
    public static boolean getIsLockedOn(ItemStack stack) { return getValueBool(stack, KEY_LOCKEDON); }
    public static void setIsLockedOn(ItemStack stack, boolean value) { setValueBool(stack, KEY_LOCKEDON, value); }
    // ANIM TRACKING //
    public static GunAnimation getLastAnim(ItemStack stack, int index) { return EnumUtil.grabEnumSafely(GunAnimation.class, getValueInt(stack, KEY_LASTANIM + index)); }
    public static void setLastAnim(ItemStack stack, int index, GunAnimation value) { setValueInt(stack, KEY_LASTANIM + index, value.ordinal()); }
    public static int getAnimTimer(ItemStack stack, int index) { return getValueInt(stack, KEY_ANIMTIMER + index); }
    public static void setAnimTimer(ItemStack stack, int index, int value) { setValueInt(stack, KEY_ANIMTIMER + index, value); }

    // BUTTON STATES //
    public static boolean getPrimary(ItemStack stack, int index) { return getValueBool(stack, KEY_PRIMARY + index); }
    public static void setPrimary(ItemStack stack, int index, boolean value) { setValueBool(stack, KEY_PRIMARY + index, value); }
    public static boolean getSecondary(ItemStack stack, int index) { return getValueBool(stack, KEY_SECONDARY + index); }
    public static void setSecondary(ItemStack stack, int index, boolean value) { setValueBool(stack, KEY_SECONDARY + index, value); }
    public static boolean getTertiary(ItemStack stack, int index) { return getValueBool(stack, KEY_TERTIARY + index); }
    public static void setTertiary(ItemStack stack, int index, boolean value) { setValueBool(stack, KEY_TERTIARY + index, value); }
    public static boolean getReloadKey(ItemStack stack, int index) { return getValueBool(stack, KEY_RELOAD + index); }
    public static void setReloadKey(ItemStack stack, int index, boolean value) { setValueBool(stack, KEY_RELOAD + index, value); }
    // RELOAD CANCEL //
    public static boolean getReloadCancel(ItemStack stack) { return getValueBool(stack, KEY_CANCELRELOAD); }
    public static void setReloadCancel(ItemStack stack, boolean value) { setValueBool(stack, KEY_CANCELRELOAD, value); }
    // EQUIPPED //
    public static boolean getIsEquipped(ItemStack stack) { return getValueBool(stack, KEY_EQUIPPED); }
    public static void setIsEquipped(ItemStack stack, boolean value) { setValueBool(stack, KEY_EQUIPPED, value); }


    /// UTIL ///
    public static int getValueInt(ItemStack stack, String name) { return TagsUtil.getCustomData(stack).getInt(name); }
    public static void setValueInt(ItemStack stack, String name, int value) { CompoundTag tag = TagsUtil.getCustomData(stack); tag.putInt(name, value); TagsUtil.putCustomData(stack, tag); }

    public static float getValueFloat(ItemStack stack, String name) { return TagsUtil.getCustomData(stack).getFloat(name); }
    public static void setValueFloat(ItemStack stack, String name, float value) { CompoundTag tag = TagsUtil.getCustomData(stack); tag.putFloat(name, value); TagsUtil.putCustomData(stack, tag); }

    public static byte getValueByte(ItemStack stack, String name) { return TagsUtil.getCustomData(stack).getByte(name); }
    public static void setValueByte(ItemStack stack, String name, byte value) { CompoundTag tag = TagsUtil.getCustomData(stack); tag.putByte(name, value); TagsUtil.putCustomData(stack, tag); }

    public static boolean getValueBool(ItemStack stack, String name) { return TagsUtil.getCustomData(stack).getBoolean(name); }
    public static void setValueBool(ItemStack stack, String name, boolean value) { CompoundTag tag = TagsUtil.getCustomData(stack); tag.putBoolean(name, value); TagsUtil.putCustomData(stack, tag); }

    /** Wrapper for extra context used in most Consumer lambdas which are part of the guncfg */
    public static class LambdaContext {
        public final GunConfig config;
        public final LivingEntity entity;
        public final Container container;
        public final int configIndex;

        public LambdaContext(GunConfig config, LivingEntity player, Container container, int configIndex) {
            this.config = config;
            this.entity = player;
            this.container = container;
            this.configIndex = configIndex;
        }

        public Player getPlayer() {
            if(!(entity instanceof Player player)) return null;
            return player;
        }
    }

    @Override
    public void renderHUD(Pre event, Player player, ItemStack stack) {

    }

    public static class SmokeNode {

        public double forward = 0D;
        public double side = 0D;
        public double lift = 0D;
        public double alpha;
        public double width = 1D;

        public SmokeNode(double alpha) { this.alpha = alpha; }
    }
}
