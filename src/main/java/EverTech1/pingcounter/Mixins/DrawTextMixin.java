package EverTech1.pingcounter.Mixins;

import EverTech1.pingcounter.Config;
import EverTech1.pingcounter.Pinger;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public class DrawTextMixin {
    @Inject(method="render", at=@At("TAIL"))
    private void renderCustomText(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(Config.enabled){
            Font font = Minecraft.getInstance().font;
            final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
            final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue + 0x1000000*Config.backgroundColorAlpha;
            final int[] pos = {20, 20};
            final String displayString = String.format(Config.displayText, Pinger.latency);
            final String measureString = String.format(Config.displayText, 999);
            final int stringSize = font.width(measureString);
            final double scale = Config.scale;
            guiGraphics.fill(pos[0]-(int)(5*scale), pos[1]-(int)(5*scale), pos[0]+(int)((stringSize+5)*scale), pos[1]+(int)((font.lineHeight+4)*scale), backgroundColor);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale((float)scale, (float)scale, (float)scale);
            guiGraphics.drawString(font, String.format(displayString, Pinger.latency), (int)(pos[0]/scale), (int)(pos[1]/scale), textColor, Config.textShadow);
            guiGraphics.pose().popPose();
        }
    }
}
