package com.felpslipe.bbb.config;

import com.felpslipe.bbb.BringBlockingBack;
import com.google.gson.Gson;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.felpslipe.bbb.BringBlockingBack.LOGGER;
import static com.felpslipe.bbb.BringBlockingBack.client;

public class ConfigManager {
    public static final Gson gson = new Gson();
    public static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("bringblockingback.json");
    public static Config config = new Config();

    public static void load() {
        if(!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath.getParent());
                try(Writer writer = new FileWriter(configPath.toFile())) {
                    gson.toJson(config, writer);

                }
            }
            catch(Exception e) {
                LOGGER.error("[BBB] Failed to write config", e);
            }
        }
        try(Reader reader = new FileReader(configPath.toFile())) {
            config = gson.fromJson(reader, Config.class);
            LOGGER.info("[BBB] Config loaded successfully");
            if(client.player != null) {
                client.player.displayClientMessage(Component.literal("[BBB] Config Reloaded"), false);
            }
        }
        catch(Exception e) {
            LOGGER.error("[BBB] Failed to load config", e);
        }

    }
}
