package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Events {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (Main.keyMap.consumeClick()) {
              Minecraft.getInstance().setScreen(new SettingsGui(CommonComponents.EMPTY, Minecraft.getInstance().screen));
            }
        }
    }

    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event){
        Main.pinger.startPinging(2000);
    }

    @SubscribeEvent
    public static void onLeaveServer(ClientPlayerNetworkEvent.LoggingOut event){
        Main.pinger.stopPinging();
    }

}