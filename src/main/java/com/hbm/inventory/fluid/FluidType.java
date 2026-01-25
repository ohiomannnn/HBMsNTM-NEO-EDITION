package com.hbm.inventory.fluid;

import api.hbm.fluidmk2.FluidNetMK2;
import com.hbm.HBMsNTM;
import com.hbm.inventory.fluid.tank.FluidTank;
import com.hbm.inventory.fluid.trait.FT_Corrosive;
import com.hbm.inventory.fluid.trait.FluidTrait;
import com.hbm.inventory.fluid.trait.FluidTraitSimple.*;
import com.hbm.render.util.EnumSymbol;
import com.hbm.uninos.INetworkProvider;
import com.hbm.uninos.networkproviders.FluidNetProvider;
import com.hbm.util.i18n.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FluidType {

    //The numeric ID of the fluid
    private int id;
    //The internal name
    private String stringId;
    //Approximate HEX Color of the fluid, used for pipe rendering
    private int color;
    //Unlocalized string ID of the fluid
    private String unlocalized;
    //localization override for custom fluids
    private String localizedOverride;
    private int guiTint = 0xffffff;

    public int poison;
    public int flammability;
    public int reactivity;
    public EnumSymbol symbol;
    public boolean renderWithTint = false;

    public static final int ROOM_TEMPERATURE = 20;

    // v v v this entire system is a pain in the ass to work with. i'd much rather define state transitions and heat values manually.
    /** How hot this fluid is. Simple enough. */
    public int temperature = ROOM_TEMPERATURE;

    public HashMap<Class<?>, Object> containers = new HashMap<>();
    public HashMap<Class<? extends FluidTrait>, FluidTrait> traits = new HashMap<>();

    private ResourceLocation texture;

    public FluidType(String name, int color, int p, int f, int r, EnumSymbol symbol) {
        this.stringId = name;
        this.color = color;
        this.unlocalized = "hbmfluid." + name.toLowerCase(Locale.US);
        this.poison = p;
        this.flammability = f;
        this.reactivity = r;
        this.symbol = symbol;
        this.texture = HBMsNTM.withDefaultNamespaceNT("textures/gui/fluids/" + name.toLowerCase(Locale.US) + ".png");

        this.id = Fluids.registerSelf(this);
    }

    /** For custom fluids */
    public FluidType(String name, int color, int p, int f, int r, EnumSymbol symbol, String texName, int tint, int id, String displayName) {
        this.setupCustom(name, color, p, f, r, symbol, texName, tint, id, displayName);
    }

    public FluidType setupCustom(String name, int color, int p, int f, int r, EnumSymbol symbol, String texName, int tint, int id, String displayName) {
        this.stringId = name;
        this.color = color;
        this.unlocalized = "hbmfluid." + name.toLowerCase(Locale.US);
        this.poison = p;
        this.flammability = f;
        this.reactivity = r;
        this.symbol = symbol;
        this.texture = HBMsNTM.withDefaultNamespaceNT("textures/gui/fluids/" + texName + ".png");
        this.guiTint = tint;
        this.localizedOverride = displayName;
        this.renderWithTint = true;

        this.id = id;
        Fluids.register(this, id);
        return this;
    }

    public FluidType(int forcedId, String name, int color, int p, int f, int r, EnumSymbol symbol) {
        this(name, color, p, f, r, symbol);

        if (this.id != forcedId) {
            throw new IllegalStateException("Howdy! I am a safeguard put into place by Bob to protect you, the player, from Bob's dementia. For whatever reason, Bob decided to either add or remove a fluid in a way that shifts the IDs, despite the entire system being built to prevent just that. Instead of people's fluids getting jumbled for the 500th time, I am here to prevent the game from starting entirely. The expected ID was " + forcedId + ", but turned out to be " + this.id + ".");
        }
    }

    /** For CompatFluidRegistry */
    public FluidType(String name, int id, int color, int p, int f, int r, EnumSymbol symbol, ResourceLocation texture) {
        setupForeign(name, id, color, p, f, r, symbol, texture);

        Fluids.foreignFluids.add(this);
        Fluids.metaOrder.add(this);
    }

    public FluidType setupForeign(String name, int id, int color, int p, int f, int r, EnumSymbol symbol, ResourceLocation texture) {
        this.stringId = name;
        this.color = color;
        this.unlocalized = "hbmfluid." + name.toLowerCase(Locale.US);
        this.poison = p;
        this.flammability = f;
        this.reactivity = r;
        this.symbol = symbol;
        this.texture = texture;
        this.renderWithTint = true;

        this.id = id;
        Fluids.register(this, id);
        return this;
    }

    public FluidType setTemp(int temperature) {
        this.temperature = temperature;
        return this;
    }

    public FluidType addContainers(Object... containers) {
        for(Object container : containers) this.containers.put(container.getClass(), container);
        return this;
    }

    public <T> T getContainer(Class<? extends T> container) {
        return (T) this.containers.get(container);
    }

    public FluidType addTraits(FluidTrait... traits) {
        for (FluidTrait trait : traits) this.traits.put(trait.getClass(), trait);
        return this;
    }

    public boolean hasTrait(Class<? extends FluidTrait> trait) {
        return this.traits.containsKey(trait);
    }

    public <T extends FluidTrait> T getTrait(Class<? extends T> trait) { //generics, yeah!
        return (T) this.traits.get(trait);
    }

    public int getID() {
        return this.id;
    }
    /** The unique mapping name for this fluid, usually matches the unlocalied name, minus the prefix */
    public String getInternalName() {
        return this.stringId;
    }

    public int getColor() {
        return this.color;
    }

    public int getTint() {
        return this.guiTint;
    }

    public ResourceLocation getTexture() {
        return this.texture;
    }
    public String getUnlocalizedName() {
        return this.unlocalized;
    }

    /** Returns the localized override name if present, or otherwise the I18n converted name */
    public Component getName() {
        return this.localizedOverride != null ? Component.literal(localizedOverride) : Component.translatable(this.unlocalized);
    }

    public boolean isHot() {
        return this.temperature >= 100;
    }
    public boolean isCorrosive() {
        return this.traits.containsKey(FT_Corrosive.class);
    }
    public boolean isAntimatter() {
        return this.traits.containsKey(FT_Amat.class);
    }
    public boolean hasNoContainer() {
        return this.traits.containsKey(FT_NoContainer.class);
    }
    public boolean hasNoID() {
        return this.traits.containsKey(FT_NoID.class);
    }
    public boolean needsLeadContainer() {
        return this.traits.containsKey(FT_LeadContainer.class);
    }
    public boolean isDispersable() {
        return !(this.traits.containsKey(FT_Amat.class) || this.traits.containsKey(FT_NoContainer.class) || this.traits.containsKey(FT_Viscous.class));
    }

    /**
     * Called when the tile entity is broken, effectively voiding the fluids.
     */
    public void onTankBroken(BlockEntity be, FluidTank tank) { }
    /**
     * Called by the tile entity's update loop. Also has an arg for the fluid tank for possible tanks using child-classes that are shielded or treated differently.
     */
    public void onTankUpdate(BlockEntity be, FluidTank tank) { }
    /**
     * For when the tile entity is releasing this fluid into the world, either by an overflow or (by proxy) when broken.
     */
    public void onFluidRelease(BlockEntity be, FluidTank tank, int overflowAmount) {
        this.onFluidRelease(be.getLevel(), be.getBlockPos(), tank, overflowAmount);
    }

    public void onFluidRelease(Level level, BlockPos pos, FluidTank tank, int overflowAmount) { }

    @OnlyIn(Dist.CLIENT)
    public void addInfo(List<Component> info) {

        if (temperature != ROOM_TEMPERATURE) {
            if (temperature < 0) info.add(Component.literal(temperature + "°C").withStyle(ChatFormatting.BLUE));
            if (temperature > 0) info.add(Component.literal(temperature + "°C").withStyle(ChatFormatting.RED));
        }

        boolean shiftHeld = Screen.hasShiftDown();

        List<Component> hidden = new ArrayList<>();

        for (Class<? extends FluidTrait> clazz : FluidTrait.traitList) {
            FluidTrait trait = this.getTrait(clazz);
            if (trait != null) {
                trait.addInfo(info);
                if (shiftHeld) trait.addInfoHidden(info);
                trait.addInfoHidden(hidden);
            }
        }

        if (!hidden.isEmpty() && !shiftHeld) {
            info.add(
                    Component.literal("Hold <").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
                            .append(Component.literal("LSHIFT").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC))
                            .append(Component.literal("> to display more info").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC))
            );
        }
    }

    //shitty wrapper delegates, go!
    //only used for compatibility purposes, these will be removed soon
    //don't use these, dumbfuck
    @Deprecated //reason: not an enum, asshole, use the registry
    public static FluidType getEnum(int i) {
        return Fluids.fromID(i);
    }
    @Deprecated //reason: the more time you waste reading this the less time is there for you to use that fucking registry already
    public static FluidType getEnumFromName(String s) {
        return Fluids.fromName(s);
    }
    @Deprecated //reason: not an enum, again, fuck you
    public int ordinal() {
        return this.getID();
    }
    @Deprecated
    public int getMSAColor() {
        return this.color;
    }
    @Deprecated
    public String name() {
        return this.stringId;
    }

    protected INetworkProvider<FluidNetMK2> NETWORK_PROVIDER = new FluidNetProvider(this);

    public INetworkProvider<FluidNetMK2> getNetworkProvider() {
        return NETWORK_PROVIDER;
    }
}
