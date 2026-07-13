package com.hbm.main;

import com.hbm.util.i18n.I18nServer;
import com.hbm.util.i18n.ITranslate;
import com.hbm.util.particle.IParticleCreator;
import com.hbm.util.particle.ParticleCreatorServer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.Nullable;

public class ServerProxy {

    private static final I18nServer I18N = new I18nServer();
    private static final IParticleCreator PARTICLE_CREATOR = new ParticleCreatorServer();

    //sort by estimated time of display. longer lasting ones should be sorted at the top.
    public static final int ID_DUCK = 0;
    public static final int ID_FILTER = 1;
    public static final int ID_COMPASS = 2;
    public static final int ID_CABLE = 3;
    public static final int ID_DRONE = 4;
    public static final int ID_JETPACK = 5;
    public static final int ID_MAGNET = 6;
    public static final int ID_HUD = 7;
    public static final int ID_DETONATOR = 8;
    public static final int ID_FLUID_ID = 9;
    public static final int ID_FAN_MODE = 10;
    public static final int ID_TOOLABILITY = 11;
    public static final int ID_GAS_HAZARD = 12;
    public static final int ID_WRENCH = 13;
    public static final int ID_PAGER_DYN = 1000;

    public ITranslate getI18n() { return I18N; }

    public IParticleCreator getParticleCreator() { return PARTICLE_CREATOR; }

    public void registerBlockEntityRenderers() { }
    public void registerClientExtensions(RegisterClientExtensionsEvent event) { }
    public void registerEntityRenderers() { }

    public void vanish(int entityId) {  }
    public void vanish(int entityId, int duration) {  }
    public boolean isVanished(Entity e) { return false; }

    public void playLocalSound(Vec3 vec, SoundEvent soundEvent, SoundSource source, float volume, float pitch) { }
    public void playLocalSound(double x, double y, double z, SoundEvent soundEvent, SoundSource source, float volume, float pitch) { }

    public void openScreen(Player player, BlockPos pos) { }

    public void displayTooltip(Component message, int id) {
        this.displayTooltip(message, 1000, id);
    }

    public void displayTooltip(Component message, int time, int id) { }

    @Nullable
    public Player me() {
        return null;
    }
}
