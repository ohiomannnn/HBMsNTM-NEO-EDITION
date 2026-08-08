package com.hbm.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class NtmConfig {
    // COMMON
    public static final CommonConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    // SERVER
    public static final ServerConfig SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    // CLIENT
    public static final ClientConfig CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        // COMMON
        Pair<CommonConfig, ModConfigSpec> commonPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        // SERVER
        Pair<ServerConfig, ModConfigSpec> serverPair = new ModConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();

        // CLIENT
        Pair<ClientConfig, ModConfigSpec> clientPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
        container.registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
    }

    // the funny
    public static boolean enable528() { return NtmConfig.COMMON.ENABLE_528.get(); }
    public static boolean true528() { return NtmConfig.COMMON.ENABLE_528.get() && NtmConfig.COMMON.ENABLE_528_MACHINE_GRAVITY.get(); }

    public static boolean enable528MachineGravity() { return NtmConfig.COMMON.ENABLE_528.get() && NtmConfig.COMMON.ENABLE_528_MACHINE_GRAVITY.get(); }

    public static boolean enableLBSM() { return !NtmConfig.COMMON.ENABLE_528.get() && NtmConfig.COMMON.ENABLE_LBSM.get(); }
}
