package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EditPositionGui extends Screen {
    private Minecraft mc;
    private final Screen parent;
    protected EditPositionGui(Component pTitle, Screen parent) {
        super(pTitle);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        mc = Minecraft.getInstance();
        addRenderableWidget(new Button.Builder(Component.literal("Back"), buttonOnPress(0)).pos((width/2)-(font.width("Back")/2)-15, height-30).size(font.width("Back")+30, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Reset"), buttonOnPress(1)).pos((width/2)-(font.width("Reset")/2)-15, height-60).size(font.width("Reset")+30, 20).build());

    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(0, 0, width, height, -11, 0xA0000000);
        Font font = mc.font;
        pGuiGraphics.pose().pushPose();
        final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
        final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue + 0x1000000*Config.backgroundColorAlpha;
        final int[] pos = {(int) (mc.getWindow().getGuiScaledWidth()*Config.posX), (int) (mc.getWindow().getGuiScaledHeight()*Config.posY)};
        final String displayString = String.format(Config.displayText, Pinger.latency);
        final String measureString = String.format(Config.displayText, 999);
        final int stringSize = font.width(measureString);
        final double scale = 3*Config.scale/mc.getWindow().getGuiScale();
        pGuiGraphics.pose().translate(0, 0, -10);
        pGuiGraphics.fill(pos[0]-(int)(5*scale), pos[1]-(int)(5*scale), pos[0]+(int)((stringSize+5)*scale), pos[1]+(int)((font.lineHeight+4)*scale), backgroundColor);
        pGuiGraphics.pose().scale((float)scale, (float)scale, 1);
        pGuiGraphics.drawString(font, String.format(displayString, Pinger.latency), (float)(pos[0]/scale), (float)(pos[1]/scale), textColor, Config.textShadow);
        pGuiGraphics.pose().popPose();
        for(Renderable renderable : this.renderables){
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        final int[] pos = {(int) (mc.getWindow().getGuiScaledWidth()*Config.posX), (int) (mc.getWindow().getGuiScaledHeight()*Config.posY)};
        final double scale = 3*Config.scale/mc.getWindow().getGuiScale();
        final int stringSize = font.width(String.format(Config.displayText, 999));
        if(pMouseX-pDragX>=pos[0]-(int)(5*scale) && pMouseX-pDragX<=pos[0]+(int)((stringSize+5)*scale) && pMouseY-pDragY>=pos[1]-(int)(5*scale) && pMouseY-pDragY<=pos[1]+(int)((font.lineHeight+4)*scale)){
            Config.posX = Math.min(Math.max(Config.posX+pDragX/width, 0), 1);
            Config.posY = Math.min(Math.max(Config.posY+pDragY/height, 0), 1);

        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    private Button.OnPress buttonOnPress(int id){
        return pButton -> {
            if(id==0){
                mc.setScreen(parent);
            }else{
                Config.posX = 0.028;
                Config.posY = 0.05;
            }
        };
    }

    @Override
    public void onClose() {
        Config.updateConfig();
        super.onClose();
    }
}
