package EverTech1.pingcounter;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Events {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (Main.keyMap.consumeClick()) {
              Minecraft.getInstance().setScreen(new SettingsGui(CommonComponents.EMPTY));
            }
        }
    }
    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event){
        Main.pinger.startPinging(2000);
        if(!Main.notified){
            ModList.get().getModContainerById(Main.MODID).ifPresent(modContainer -> {
                VersionChecker.CheckResult res = VersionChecker.getResult(modContainer.getModInfo());
                if(res.status().isOutdated()){
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