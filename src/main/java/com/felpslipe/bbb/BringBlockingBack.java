package com.felpslipe.bbb;

import com.felpslipe.bbb.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BringBlockingBack implements ClientModInitializer {
	public static final String MOD_ID = "bring-blocking-back";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Minecraft client;
	@Override
	public void onInitializeClient() {
		client = Minecraft.getInstance();
		LOGGER.info("[BBB] Bringing blocking back..");
		ConfigManager.load();
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("bbb")
					.executes(context -> {
				context.getSource().sendFeedback(Component.literal("BBB commands:"));
				context.getSource().sendFeedback(Component.literal("reload - reloads config"));
				return 1;
			})
							.then(ClientCommandManager.literal("reload")
							.executes(context -> {
								ConfigManager.load();
								return 1;
							}))
			);
		});
	}
}