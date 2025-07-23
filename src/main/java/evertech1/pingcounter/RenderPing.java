package evertech1.pingcounter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class RenderPing {
    private static final Config cfg = ConfigHandler.config;
    public static void render(DrawContext context, RenderTickCounter tickCounter){
        if(cfg.enabled && !PingCounter.isEditing && !MinecraftClient.getInstance().isConnectedToLocalServer() && !MinecraftClient.getInstance().getDebugHud().shouldShowDebugHud()) {
            renderOverlay(context);
        }
    }
    public static void renderOverlay(DrawContext context){
        final double scale = (3*cfg.scale/MinecraftClient.getInstance().getWindow().getScaleFactor());
        final float[] pos = {(float) (context.getScaledWindowWidth() * cfg.posX), (float) (context.getScaledWindowHeight() * cfg.posY)};
        final String displayString = String.format(cfg.displayText, Pinger.ping);
        final String measureString = String.format(cfg.displayText, 999);
        final int stringSize = MinecraftClient.getInstance().textRenderer.getWidth(measureString);
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(pos[0], pos[1]);
        context.getMatrices().scale((float) scale, (float) scale);
        int bgColor = cfg.backgroundColorAlpha * 0x01000000 + cfg.backgroundColorRed * 0x010000 + cfg.backgroundColorGreen * 0x0100 + cfg.backgroundColorBlue;
        int txtColor = 0xFF000000 + cfg.textColorRed * 0x010000 + cfg.textColorGreen * 0x0100 + cfg.textColorBlue;
        context.fill(-5, -5, 5 + stringSize, MinecraftClient.getInstance().textRenderer.fontHeight + 4, bgColor);
        context.drawText(MinecraftClient.getInstance().textRenderer, displayString, 0, 0, txtColor, cfg.textShadow);
        context.getMatrices().popMatrix();
    }
}
