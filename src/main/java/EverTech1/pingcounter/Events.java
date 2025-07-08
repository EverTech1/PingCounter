package EverTech1.pingcounter;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class Events {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (Main.keyMap.consumeClick()) {
          Minecraft.getInstance().setScreen(new SettingsGui(CommonComponents.EMPTY));
        }
    }
    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event){
        Pinger.connection = event.getConnection();
        Main.pinger.startPinging(2000);
        if(!Main.notified){
            ModList.get().getModContainerById(Main.MODID).ifPresent(modContainer -> {
                VersionChecker.CheckResult res = VersionChecker.getResult(modContainer.getModInfo());
                if(res.status() == VersionChecker.Status.OUTDATED){
                    if(Minecraft.getInstance().player != null){
                        Minecraft.getInstance().player.displayClientMessage(Component.literal("Ping Counter mod is outdated. Download latest version ").append(Component.literal("here").withStyle(style -> style.withColor(ChatFormatting.AQUA).withUnderlined(true).withClickEvent(ClickEventCompat.createOpenUrl(res.url())))), false);
                    }
                    Main.notified = true;
                }
                else if(res.status().equals(VersionChecker.Status.UP_TO_DATE)){
                    Main.notified = true;
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLeaveServer(ClientPlayerNetworkEvent.LoggingOut event){
        Pinger.connection = null;
        Main.pinger.stopPinging();
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event){
        event.getDispatcher().register(
                Commands.literal("pingcounter").executes(contex->{
                    Minecraft.getInstance().setScreen(new SettingsGui(CommonComponents.EMPTY));
                    return 1;
                })
        );
    }

}