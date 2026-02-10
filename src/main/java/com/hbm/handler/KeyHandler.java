package com.hbm.handler;

import com.hbm.HBMsNTM;
import com.hbm.HBMsNTMClient;
import com.hbm.config.MainConfig;
import com.hbm.extprop.HbmPlayerAttachments;
import com.hbm.inventory.screens.CalculatorScreen;
import com.hbm.inventory.screens.SatelliteInterfaceScreen;
import com.hbm.items.IKeybindReceiver;
import com.hbm.network.toserver.KeybindReceiver;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@EventBusSubscriber(modid = HBMsNTM.MODID, value = Dist.CLIENT)
public class KeyHandler {

    private static final String category = "hbmsntm.keys";

    public static final KeyMapping CALCULATOR = new KeyMapping(category + ".calc", InputConstants.Type.KEYSYM, InputConstants.KEY_N, category);
    public static final KeyMapping JETPACK = new KeyMapping(category + ".toggleBack", InputConstants.Type.KEYSYM, InputConstants.KEY_C, category);
    public static final KeyMapping MAGNET = new KeyMapping(category + ".toggleMagnet", InputConstants.Type.KEYSYM, InputConstants.KEY_Z, category);
    public static final KeyMapping HUD = new KeyMapping(category + ".toggleHUD", InputConstants.Type.KEYSYM, InputConstants.KEY_V, category);

    public static final KeyMapping DUCK = new KeyMapping(category + ".duck", InputConstants.Type.KEYSYM, InputConstants.KEY_O, category);

    public static final KeyMapping ABILITY_CYCLE = new KeyMapping(category + ".ability", InputConstants.Type.MOUSE, InputConstants.MOUSE_BUTTON_RIGHT, category);
    public static final KeyMapping ABILITY_ALT = new KeyMapping(category + ".abilityAlt", InputConstants.Type.KEYSYM, InputConstants.KEY_LALT, category);


    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CALCULATOR);
        event.register(JETPACK);
        event.register(MAGNET);
        event.register(HUD);

        event.register(ABILITY_CYCLE);
        event.register(ABILITY_ALT);
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        boolean state = (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_REPEAT);

        /// OVERLAP HANDLING ///
        handleOverlap(state, event.getButton());

        /// KEYBIND PROPS ///
        handleProps(state, event.getButton());
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        boolean state = event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_RELEASE;

        /// OVERLAP HANDLING ///
        handleOverlap(state, event.getKey());

        /// KEYBIND PROPS ///
        handleProps(state, event.getKey());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.screen != null) return;

        if (CALCULATOR.isDown()) {
            mc.setScreen(new CalculatorScreen());
        }

        HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);

        if (mc.options.keyUse.getKey() == ABILITY_CYCLE.getKey()) {
            boolean last = props.getKeyPressed(EnumKeybind.ABILITY_CYCLE);
            boolean current = ABILITY_CYCLE.isDown();

            if (last != current) {
                props.setKeyPressed(player, EnumKeybind.ABILITY_CYCLE, current);
                PacketDistributor.sendToServer(new KeybindReceiver(EnumKeybind.ABILITY_CYCLE, current));
                onPressedClient(player, EnumKeybind.ABILITY_CYCLE, current);
            }
        }
    }

    /** Handles keybind overlap. Make sure this runs first before referencing the keybinds set by the extprops */
    public static void handleOverlap(boolean state, int keyCode) {
        Minecraft mc = Minecraft.getInstance();
        if (MainConfig.COMMON.ENABLE_KEYBIND_OVERLAP.get() && mc.screen == null) {
            for (Entry<String, KeyMapping> entry : KeyMapping.ALL.entrySet()) {
                KeyMapping key = entry.getValue();

                if (keyCode != -1 && key.getKey().getValue() == keyCode && !KeyMapping.ALL.containsValue(key)) {

                    key.setDown(state);
                    if (state && key.clickCount == 0) {
                        key.clickCount = 1;
                    }
                }
            }
        }
    }

    public static void handleProps(boolean state, int keyCode) {

        /// KEYBIND PROPS ///
        LocalPlayer player = HBMsNTMClient.me();
        if (player == null) return;
        HbmPlayerAttachments props = HbmPlayerAttachments.getData(player);

        for (EnumKeybind key : EnumKeybind.values()) {
            boolean last = props.getKeyPressed(key);
            boolean current = getIsKeyPressed(key);

            if (last != current) {

                /// ABILITY HANDLING ///
                if (key == EnumKeybind.ABILITY_CYCLE && Minecraft.getInstance().options.keyUse.getKey() == ABILITY_CYCLE.getKey()) continue;

                props.setKeyPressed(player, key, current);
                PacketDistributor.sendToServer(new KeybindReceiver(key, current));
                onPressedClient(player, key, current);
            }
        }
    }

    public static void onPressedClient(LocalPlayer player, EnumKeybind key, boolean state) {
        // ITEM HANDLING
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(held.getItem() instanceof IKeybindReceiver rec) {
            if (rec.canHandleKeybind(player, held, key)) rec.handleKeybindClient(player, held, key, state);
        }
    }


    public static boolean getIsKeyPressed(EnumKeybind key) {

        return switch (key) {
            case JETPACK -> Minecraft.getInstance().options.keyJump.isDown();
            case TOGGLE_JETPACK -> JETPACK.isDown();
            case TOGGLE_MAGNET -> MAGNET.isDown();
            case TOGGLE_HEAD -> HUD.isDown();
            case ABILITY_CYCLE -> ABILITY_CYCLE.isDown();
            case ABILITY_ALT -> ABILITY_ALT.isDown();
            default -> false;
        };

    }

    public enum EnumKeybind {
        JETPACK,
        TOGGLE_JETPACK,
        TOGGLE_MAGNET,
        TOGGLE_HEAD,
        DUCK,
        DASH,
        TRAIN,
        CRANE_UP,
        CRANE_DOWN,
        CRANE_LEFT,
        CRANE_RIGHT,
        CRANE_LOAD,
        ABILITY_CYCLE,
        ABILITY_ALT,
        TOOL_ALT,
        TOOL_CTRL,
        GUN_PRIMARY,
        GUN_SECONDARY,
        GUN_TERTIARY,
        RELOAD
    }
}
