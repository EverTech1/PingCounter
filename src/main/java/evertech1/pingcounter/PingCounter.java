package evertech1.pingcounter;

import evertech1.pingcounter.GUI.SettingsGui;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingCounter implements ModInitializer {
	public static final String MOD_ID = "pingcounter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier EXAMPLE_LAYER = Identifier.of(PingCounter.MOD_ID, "ping-display-layer");
	private static KeyBinding keybind;
	public static boolean isEditing = false;
	@Override
	public void onInitialize() {
		keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.pingcounter.open_settings",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_MINUS,
				"key.categories.pingcounter"
		));
		ConfigHandler.getValues();
		Pinger.startPinger(1000);
		HudLayerRegistrationCallback.EVENT.register(layeredDrawer->layeredDrawer.attachLayerAfter(IdentifiedLayer.CHAT, EXAMPLE_LAYER, RenderPing::render));

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client)->{
			Pinger.sender = sender;
			Pinger.isConnected = true;
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client)->{
			Pinger.sender = null;
			Pinger.isConnected = false;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keybind.wasPressed()) {
				MinecraftClient.getInstance().setScreen(new SettingsGui(Text.empty()));
			}
		});
	}
}