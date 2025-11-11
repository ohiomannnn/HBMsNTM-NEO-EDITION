package com.hbm.handler;

import com.hbm.HBMsNTM;
import com.hbm.items.IKeybindReceiver;
import com.hbm.network.toserver.KeybindReceiver;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@EventBusSubscriber(modid = HBMsNTM.MODID, value = Dist.CLIENT)
public class KeyHandler {

    private static final String category = "hbmsntm.keys";

    public static final KeyMapping JETPACK = new KeyMapping(category + ".toggleBack", InputConstants.Type.KEYSYM, InputConstants.KEY_C, category);
    public static final KeyMapping MAGNET = new KeyMapping(category + ".toggleMagnet", InputConstants.Type.KEYSYM, InputConstants.KEY_Z, category);
    public static final KeyMapping HUD = new KeyMapping(category + ".toggleHUD", InputConstants.Type.KEYSYM, InputConstants.KEY_V, category);

    public static final KeyMapping ABILITY_CYCLE = new KeyMapping(category + ".ability", InputConstants.Type.MOUSE, InputConstants.MOUSE_BUTTON_RIGHT, category);
    public static final KeyMapping ABILITY_ALT = new KeyMapping(category + ".abilityAlt", InputConstants.Type.KEYSYM, InputConstants.KEY_LALT, category);
    public static final KeyMapping COPY_TOOL_ALT = new KeyMapping(category + ".copyToolAlt", InputConstants.Type.KEYSYM, InputConstants.KEY_LALT, category);
    public static final KeyMapping COPY_TOOL_CTRL = new KeyMapping(category + ".copyToolCtrl", InputConstants.Type.KEYSYM, InputConstants.KEY_LCONTROL, category);

    private static final Map<KeyMapping, EnumKeybind> CONNECTED_BINDS = new HashMap<>();

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        register(event, JETPACK, EnumKeybind.TOGGLE_JETPACK);
        register(event, MAGNET, EnumKeybind.TOGGLE_MAGNET);
        register(event, HUD, EnumKeybind.TOGGLE_HEAD);
        register(event, ABILITY_CYCLE, EnumKeybind.ABILITY_CYCLE);
        register(event, ABILITY_ALT, EnumKeybind.ABILITY_ALT);
        register(event, COPY_TOOL_ALT, EnumKeybind.TOOL_ALT);
        register(event, COPY_TOOL_CTRL, EnumKeybind.TOOL_CTRL);
    }

    private static void register(RegisterKeyMappingsEvent event, KeyMapping mapping, EnumKeybind logical) {
        event.register(mapping);
        CONNECTED_BINDS.put(mapping, logical);
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().screen != null) return;
        for (Entry<KeyMapping, EnumKeybind> entry : CONNECTED_BINDS.entrySet()) {
            KeyMapping mapping = entry.getKey();
            EnumKeybind enumKeybind = entry.getValue();

            if (mapping.matches(event.getButton(), event.getAction())) {
                if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_RELEASE) {
                    boolean isPressed = event.getAction() == GLFW.GLFW_PRESS;
                    PacketDistributor.sendToServer(new KeybindReceiver(enumKeybind, isPressed, true));
                    handleKeybindReceiver(Minecraft.getInstance().player, enumKeybind, isPressed);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().screen != null) return;
        for (Entry<KeyMapping, EnumKeybind> entry : CONNECTED_BINDS.entrySet()) {
            KeyMapping mapping = entry.getKey();
            EnumKeybind enumKeybind = entry.getValue();

            if (mapping.matches(event.getKey(), event.getScanCode())) {
                if (event.getAction() == GLFW.GLFW_PRESS || event.getAction() == GLFW.GLFW_RELEASE) {
                    boolean isPressed = event.getAction() == GLFW.GLFW_PRESS;
                    PacketDistributor.sendToServer(new KeybindReceiver(enumKeybind, isPressed, true));
                    handleKeybindReceiver(Minecraft.getInstance().player, enumKeybind, isPressed);
                }
            }
        }
    }

    private static void handleKeybindReceiver(Player player, EnumKeybind key, boolean state) {
        for (ItemStack stack : new ItemStack[]{player.getMainHandItem(), player.getOffhandItem()}) {
            if (stack.getItem() instanceof IKeybindReceiver receiver) {
                if (receiver.canHandleKeybind(player, stack, key)) {
                    receiver.handleKeybindClient(player, stack, key, state);

                    PacketDistributor.sendToServer(new KeybindReceiver(key, state, false));
                }
            }
        }
    }

    public enum EnumKeybind {
        JETPACK,
        TOGGLE_JETPACK,
        TOGGLE_MAGNET,
        TOGGLE_HEAD,
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
