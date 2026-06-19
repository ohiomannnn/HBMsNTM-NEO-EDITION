package com.hbm.main;

import com.hbm.util.i18n.I18nServer;
import com.hbm.util.i18n.ITranslate;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.Nullable;

public class ServerProxy {

    private static final I18nServer I18N = new I18nServer();

    public ITranslate getI18n() { return I18N; }

    public void registerBlockEntityRenderers() { }
    public void registerClientExtensions(RegisterClientExtensionsEvent event) { }
    public void registerEntityRenderers() { }

    public void playLocalSound(Vec3 vec, SoundEvent soundEvent, SoundSource source, float volume, float pitch) { }
    public void playLocalSound(double x, double y, double z, SoundEvent soundEvent, SoundSource source, float volume, float pitch) { }

    @Nullable
    public Player me() {
        return null;
    }
}
