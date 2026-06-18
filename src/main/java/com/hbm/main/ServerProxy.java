package com.hbm.main;

import com.hbm.util.i18n.I18nServer;
import com.hbm.util.i18n.ITranslate;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.Nullable;

public class ServerProxy {

    private static final I18nServer I18N = new I18nServer();

    public ITranslate getI18n() { return I18N; }

    public void registerBlockEntityRenderers() { }
    public void registerClientExtensions(RegisterClientExtensionsEvent event) { }
    public void registerEntityRenderers() { }

    @Nullable
    public Player me() {
        return null;
    }
}
