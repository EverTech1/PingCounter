package evertech1.pingcounter;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class RenderPing {
    private static final Config cfg = ConfigHandler.config;
    public static void render(DrawContext context, RenderTickCounter tickCounter){
        if(cfg.enabled) {
            final double scale = (3 * cfg.scale / MinecraftClient.getInstance().getWindow().getScaleFactor());
            final double[] pos = {context.getScaledWindowWidth() * cfg.posX / scale, context.getScaledWindowHeight() * cfg.posY / scale};
            final String displayString = String.format(cfg.displayText, Pinger.ping);
            final String measureString = String.format(cfg.displayText, 999);
            final int stringSize = MinecraftClient.getInstance().textRenderer.getWidth(measureString);
            context.getMatrices().push();
            context.getMatrices().scale((float) scale, (float) scale, 1);
            context.getMatrices().translate(pos[0], pos[1], 0);
            int bgColor = cfg.backgroundColorAlpha*0x01000000+cfg.backgroundColorRed*0x010000+cfg.backgroundColorGreen*0x0100+cfg.backgroundColorBlue;
            int txtColor = 0xFF000000+cfg.textColorRed*0x010000+cfg.textColorGreen*0x0100+cfg.textColorBlue;
            context.fill(-5, -5, 5 + stringSize, MinecraftClient.getInstance().textRenderer.fontHeight + 4, bgColor);
            context.drawText(MinecraftClient.getInstance().textRenderer, displayString, 0, 0, txtColor, cfg.textShadow);
            context.getMatrices().pop();
        }
    }
}
