package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;


public class PositionGui extends GuiScreen {
    Minecraft mc = Minecraft.getMinecraft();

    public int scaledHeight = new ScaledResolution(this.mc).getScaledHeight();
    public int scaledWidth = new ScaledResolution(this.mc).getScaledWidth();
    public float currentX = Main.customX;
    public float currentY = Main.customY;
    GuiScreen parentGui;
    public PositionGui(GuiScreen parentGui){
        this.parentGui = parentGui;
    }
    private boolean prevEnableState = false;
    @Override
    public void initGui() {
        prevEnableState = Main.enableDisplay;
        Main.enableDisplay = false;
        super.initGui();
        buttonList.add(new GuiButton(0, (scaledWidth/2)-(mc.fontRendererObj.getStringWidth("Back")/2)-15, scaledHeight-30, mc.fontRendererObj.getStringWidth("Back")+30, 20, "Back"));
        buttonList.add(new GuiButton(1, (scaledWidth/2)-(mc.fontRendererObj.getStringWidth("Reset   ")/2)-15, scaledHeight-60, mc.fontRendererObj.getStringWidth("Reset")+30, 20, "Reset"));

    }

    @Override
    public void onGuiClosed() {
        new UpdateConfigs().updatePos();
        Main.enableDisplay = prevEnableState;
        super.onGuiClosed();
    }
    private boolean enableEdit = false;
    private int diffX = 0;
    private int diffY = 0;
    @Override
    protected void mouseClickMove(int p_mouseClickMove_1_, int p_mouseClickMove_2_, int p_mouseClickMove_3_, long p_mouseClickMove_4_) {
        if(enableEdit){
            int posX = p_mouseClickMove_1_ + diffX;
            int posY = p_mouseClickMove_2_ + diffY;
            ScaledResolution scaled = new ScaledResolution(mc);
            scaledWidth = scaled.getScaledWidth();
            scaledHeight = scaled.getScaledHeight();
            Main.customX = currentX = (float)posX/scaledWidth;
            Main.customY = currentY = (float)posY/scaledHeight;
        }
        super.mouseClickMove(p_mouseClickMove_1_, p_mouseClickMove_2_, p_mouseClickMove_3_, p_mouseClickMove_4_);
    }

    @Override
    protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException {
        if(p_mouseClicked_1_ >= currentX*scaledWidth-4 && p_mouseClicked_1_ <= currentX*scaledWidth+mc.fontRendererObj.getStringWidth("Ping: 999 ms")+4 && p_mouseClicked_2_ >= currentY*scaledHeight-4 && p_mouseClicked_2_ <= currentY*scaledHeight+mc.fontRendererObj.FONT_HEIGHT+3){
            diffX = Math.round(currentX*scaledWidth-p_mouseClicked_1_);
            diffY = Math.round(currentY*scaledHeight-p_mouseClicked_2_);
            enableEdit = true;
        }
        super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }

    @Override
    protected void mouseReleased(int p_mouseReleased_1_, int p_mouseReleased_2_, int p_mouseReleased_3_) {
        enableEdit = false;
        super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_2_, p_mouseReleased_3_);
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        drawRect(0,0, scaledWidth, scaledHeight, 0xA0000000);
        drawPing(currentX, currentY);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    @Override
    protected void actionPerformed(GuiButton p_actionPerformed_1_) throws IOException {
        switch(p_actionPerformed_1_.id){
            case 0:
                mc.displayGuiScreen(parentGui);
                break;
            case 1:
                Main.customX = currentX = 0;
                Main.customY = currentY = 0;
        }
        super.actionPerformed(p_actionPerformed_1_);
    }

    public void drawPing(float x, float y){
        int scaledX = Math.round(x*scaledWidth);
        int scaledY = Math.round(y*scaledHeight);
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glColor4f((float)Main.redValBg/255, (float)Main.greenValBg/255,(float)Main.blueValBg/255, Main.alphaValBg);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2i(scaledX-5, scaledY-5);
            GL11.glVertex2i(scaledX-5, scaledY+mc.fontRendererObj.FONT_HEIGHT+4);
            GL11.glVertex2i(scaledX+mc.fontRendererObj.getStringWidth("Ping: 999 ms")+5, scaledY+mc.fontRendererObj.FONT_HEIGHT+4);
            GL11.glVertex2i(scaledX+mc.fontRendererObj.getStringWidth("Ping: 999 ms")+5, scaledY-5);

        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawStringWithShadow("Ping: "+Events.latency+" ms", scaledX, scaledY, new Color(Main.redValText, Main.greenValText, Main.blueValText).getRGB());
    }
}
