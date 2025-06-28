package evertech1.pingcounter;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingCounter implements ModInitializer {
	public static final String MOD_ID = "pingcounter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Identifier EXAMPLE_LAYER = Identifier.of(PingCounter.MOD_ID, "ping-display-layer");
	@Override
	public void onInitialize() {
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
	}
}