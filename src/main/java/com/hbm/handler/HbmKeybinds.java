package com.hbm.handler;

import com.hbm.HBMsNTM;
import com.hbm.extprop.PlayerProperties;
import com.hbm.items.IKeybindReceiver;
import com.hbm.packets.toserver.KeybindPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = HBMsNTM.MODID, value = Dist.CLIENT)
public class HbmKeybinds {

    private static final String category = "hbmsntm.key";

    public static final KeyMapping JETPACK = new KeyMapping(category + ".toggleBack", InputConstants.Type.KEYSYM, InputConstants.KEY_C, category);

    public static final KeyMapping ABILITY_CYCLE = new KeyMapping(category + ".ability", InputConstants.Type.MOUSE, InputConstants.MOUSE_BUTTON_RIGHT, category);
    public static final KeyMapping ABILITY_ALT = new KeyMapping(category + ".abilityAlt", InputConstants.Type.KEYSYM, InputConstants.KEY_LALT, category);
    public static final KeyMapping COPY_TOOL_ALT = new KeyMapping(category + ".copyToolAlt", InputConstants.Type.KEYSYM, InputConstants.KEY_LALT, category);
    public static final KeyMapping COPY_TOOL_CTRL = new KeyMapping(category + ".copyToolCtrl", InputConstants.Type.KEYSYM, InputConstants.KEY_LCONTROL, category);

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(JETPACK);

        event.register(ABILITY_CYCLE);
        event.register(ABILITY_ALT);
        event.register(COPY_TOOL_ALT);
        event.register(COPY_TOOL_CTRL);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onMouse(InputEvent.MouseButton.Post event) {
        handleProps(true, event.getButton() - 100);
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onKeyInput(InputEvent.Key event) {
        handleProps(event.getAction() != 0, event.getKey());
    }

    private static boolean isKeyPressed(EnumKeybind key) {
        return switch (key) {
            case JETPACK -> JETPACK.isDown();
            case TOGGLE_JETPACK -> false;
            case TOGGLE_MAGNET -> false;
            case TOGGLE_HEAD -> false;
            case DASH -> false;
            case TRAIN -> false;
            case CRANE_UP -> false;
            case CRANE_DOWN -> false;
            case CRANE_LEFT -> false;
            case CRANE_RIGHT -> false;
            case CRANE_LOAD -> false;
            case ABILITY_CYCLE -> ABILITY_CYCLE.isDown();
            case ABILITY_ALT -> ABILITY_ALT.isDown();
            case TOOL_ALT -> COPY_TOOL_ALT.isDown();
            case TOOL_CTRL -> COPY_TOOL_CTRL.isDown();
            case GUN_PRIMARY -> false;
            case GUN_SECONDARY -> false;
            case GUN_TERTIARY -> false;
            case RELOAD -> false;
        };
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        PlayerProperties props = PlayerProperties.getData(player);

        if (mc.options.keyUse.getKey().getValue() == ABILITY_CYCLE.getKey().getValue()) {
            boolean last = props.getKeyPressed(EnumKeybind.ABILITY_CYCLE);
            boolean current = ABILITY_CYCLE.isDown();

            if (last != current) {
                PacketDistributor.sendToServer(new KeybindPacket(EnumKeybind.ABILITY_CYCLE, current));
                props.setKeyPressed(EnumKeybind.ABILITY_CYCLE, current);
                onPressedClient(player, EnumKeybind.ABILITY_CYCLE, current);
            }
        }
    }

    public static void handleProps(boolean state, int keyCode) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        PlayerProperties props = PlayerProperties.getData(player);

        for (EnumKeybind key : EnumKeybind.values()) {
            boolean last = props.getKeyPressed(key);
            boolean current = isKeyPressed(key);

            if (last != current) {
                if (key == EnumKeybind.ABILITY_CYCLE && Minecraft.getInstance().options.keyUse.getKey().getValue() == ABILITY_CYCLE.getKey().getValue())
                    continue;

                props.setKeyPressed(key, current);
                PacketDistributor.sendToServer(new KeybindPacket(key, current));
                onPressedClient(player, key, current);
            }
        }
    }


    public static void onPressedClient(Player player, EnumKeybind key, boolean state) {
        ItemStack held = player.getMainHandItem();
        if (!held.isEmpty() && held.getItem() instanceof IKeybindReceiver rec) {
            if (rec.canHandleKeybind(player, held, key))
                rec.handleKeybindClient(player, held, key, state);
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
