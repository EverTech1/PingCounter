package EverTech1.pingcounter;

import EverTech1.pingcounter.GUI.SettingsGui;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Events {
    private static final Minecraft mc = Minecraft.getInstance();
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (Main.keyMap.consumeClick()) {
              mc.setScreen(new SettingsGui(CommonComponents.EMPTY));
            }
        }
    }
    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggingIn event){
        Pinger.connection = event.getConnection();
        Main.pinger.startPinging(2000);
        if(!Main.notified){
            ModList.get().getModContainerById(Main.MODID).ifPresent(modContainer -> {
                VersionChecker.CheckResult res = VersionChecker.getResult(modContainer.getModInfo());
                if(res.status().isOutdated()){
                    if(mc.player != null){
                        mc.player.displayClientMessage(Component.literal("Ping Counter mod is outdated. Download latest version ").append(Component.literal("here").withStyle(style -> style.withColor(ChatFormatting.AQUA).withUnderlined(true).withClickEvent(ClickEventCompat.createOpenUrl(res.url())))), false);
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
        Pinger.connection = null;
    }

    @SubscribeEvent
    public static void registerCommands(RegisterClientCommandsEvent event){
        event.getDispatcher().register(
                Commands.literal("pingcounter").executes(contex->{
                    mc.setScreen(new SettingsGui(CommonComponents.EMPTY));
                    return 1;
                })
        );
    }

    @SubscribeEvent
    public static void onCustomizeOverlay(CustomizeGuiOverlayEvent.Chat e){
        if(Config.enabled && !Main.isEditing &&!mc.isLocalServer()&&!mc.gui.getDebugOverlay().showDebugScreen()) drawPing(e.getGuiGraphics());
    }

    public static void drawPing(GuiGraphics guiGraphics){
        guiGraphics.pose().pushPose();
        Font font = mc.font;
        final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
        final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue + 0x1000000*Config.backgroundColorAlpha;
        final double scale = 3*Config.scale/mc.getWindow().getGuiScale();
        final double[] pos = {(mc.getWindow().getGuiScaledWidth()*Config.posX), (mc.getWindow().getGuiScaledHeight()*Config.posY)};
        final String displayString = String.format(Config.displayText, Pinger.latency);
        final String measureString = String.format(Config.displayText, 999);
        final int stringSize = font.width(measureString);

        guiGraphics.pose().translate(pos[0], pos[1], 0.0);
        guiGraphics.pose().scale((float)scale, (float)scale, 1);
        guiGraphics.fill(-5, -5, stringSize+5, font.lineHeight+4, backgroundColor);
        guiGraphics.drawString(font, String.format(displayString, Pinger.latency), 0, 0, textColor, Config.textShadow);
        guiGraphics.pose().popPose();
    }
}