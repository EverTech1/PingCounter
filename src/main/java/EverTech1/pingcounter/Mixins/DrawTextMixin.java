package EverTech1.pingcounter.Mixins;

import EverTech1.pingcounter.Config;
import EverTech1.pingcounter.Pinger;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public class DrawTextMixin {
    @Inject(method="renderChat", at=@At("TAIL"))
    private void renderCustomText(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        guiGraphics.pose().pushPose();
        if(Config.enabled && !Minecraft.getInstance().isLocalServer()&&!Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()){
            Minecraft mc = Minecraft.getInstance();
            Font font = mc.font;
            final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
            final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue + 0x1000000*Config.backgroundColorAlpha;
            final int[] pos = {(int) (mc.getWindow().getGuiScaledWidth()*Config.posX), (int) (mc.getWindow().getGuiScaledHeight()*Config.posY)};
            final String displayString = String.format(Config.displayText, Pinger.latency);
            final String measureString = String.format(Config.displayText, 999);
            final int stringSize = font.width(measureString);
            final double scale = 3*Config.scale/mc.getWindow().getGuiScale();

            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor((float) Config.backgroundColorRed /255, (float) Config.backgroundColorGreen /255, (float) Config.backgroundColorBlue /255, (float) Config.backgroundColorAlpha /255);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            buffer.addVertex((float)(pos[0]-(5*scale)), (float)(pos[1]-(5*scale)), 0.0f).setColor(backgroundColor);
            buffer.addVertex((float)(pos[0]-(5*scale)), (float)(pos[1]+(font.lineHeight+4)*scale), 0.0f).setColor(backgroundColor);
            buffer.addVertex((float)(pos[0]+(stringSize+5)*scale), (float)(pos[1]+(font.lineHeight+4)*scale), 0.0f).setColor(backgroundColor);
            buffer.addVertex((float)(pos[0] +(stringSize+5)*scale), (float)(pos[1]-(5*scale)), 0.0f).setColor(backgroundColor);
            BufferUploader.drawWithShader(buffer.build());
            RenderSystem.setShaderColor(1,1,1,1);
            RenderSystem.disableBlend();

//            guiGraphics.fill(pos[0]-(int)(5*scale), pos[1]-(int)(5*scale), pos[0]+(int)((stringSize+5)*scale), pos[1]+(int)((font.lineHeight+4)*scale), backgroundColor);
            guiGraphics.pose().scale((float)scale, (float)scale, 1);
            guiGraphics.drawString(font, String.format(displayString, Pinger.latency), (float)(pos[0]/scale), (float)(pos[1]/scale), textColor, Config.textShadow);
        }
        guiGraphics.pose().popPose();
    }
}
