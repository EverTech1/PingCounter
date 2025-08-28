package evertech1.pingcounter;

import evertech1.pingcounter.GUI.SettingsGui;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

@Environment(EnvType.CLIENT)
public class PingCounter implements ModInitializer {
	public static String MOD_VERSION = "1.0.2";
	public static final String MOD_ID = "pingcounter";
	private static final Identifier PING_LAYER = Identifier.of(PingCounter.MOD_ID, "ping_layer");
	private static KeyBinding keybind;
	private static boolean shouldOpenGUI = false;
	public static boolean isEditing = false;
	private static boolean notified = false;
	private static final String downloadLink = "PLACEHOLDER_DOWNLOAD";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		VersionChecker.startCheck();

		keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.pingcounter.open_settings",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_MINUS,
				"key.categories.pingcounter"
		));
		ConfigHandler.getValues();
		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, PING_LAYER, RenderPing::render);
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client)->{
			if(!client.isIntegratedServerRunning()){
				Pinger.sender = sender;
				Pinger.isConnected = true;
				Pinger.startPinger(2000);
				if(VersionChecker.result == VersionChecker.Results.OLD && !notified){
					notified = true;
					if(client.player != null){
						client.player.sendMessage(Text.literal("Ping Counter mod is outdated. Download latest version ").append(Text.literal("here").setStyle(Style.EMPTY.withColor(5636095).withUnderline(true).withClickEvent(new ClickEvent.OpenUrl(URI.create(downloadLink))))), false);
					}
				}
			}
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client)->{
			Pinger.stopPinger();
			Pinger.sender = null;
			Pinger.isConnected = false;
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keybind.wasPressed() || shouldOpenGUI) {
				MinecraftClient.getInstance().execute(()->{
					shouldOpenGUI = false;
					MinecraftClient.getInstance().setScreen(new SettingsGui(Text.empty()));
				});
			}
		});
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("pingcounter")
			.executes(context -> {
				shouldOpenGUI = true;
				return 1;
			}))
		);
	}
}