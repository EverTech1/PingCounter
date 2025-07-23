package EverTech1.pingcounter;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;

public class PingLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if(Config.enabled && !Main.isEditing &&!Minecraft.getInstance().isLocalServer()&&!Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()){
            renderInternal(guiGraphics);
        }
    }
    public static void renderInternal(GuiGraphics guiGraphics){
        guiGraphics.pose().pushPose();
        Minecraft mc = Minecraft.getInstance();
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
