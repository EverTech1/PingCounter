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
        buttonList.add(new GuiButton(1, (scaledWidth/2)-(mc.fontRendererObj.getStringWidth("Reset")/2)-15, scaledHeight-60, mc.fontRendererObj.getStringWidth("Reset")+30, 20, "Reset"));

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
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(enableEdit){
            int posX = mouseX + diffX;
            int posY = mouseY + diffY;
            ScaledResolution scaled = new ScaledResolution(mc);
            scaledWidth = scaled.getScaledWidth();
            scaledHeight = scaled.getScaledHeight();
            Main.customX = currentX = Math.min(Math.max((float)posX/scaledWidth, 0), (float)0.99);
            Main.customY = currentY = Math.min(Math.max((float)posY/scaledHeight, 0), (float)0.99);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseX >= currentX*scaledWidth-4*Main.scalar && mouseY <= currentX*scaledWidth+(mc.fontRendererObj.getStringWidth(Main.displayText.replaceAll("(\\$\\[ping])", "999"))+4)*Main.scalar && mouseY >= currentY*scaledHeight-4*Main.scalar && mouseY <= currentY*scaledHeight+(mc.fontRendererObj.FONT_HEIGHT+3)*Main.scalar){
            diffX = Math.round(currentX*scaledWidth-mouseX);
            diffY = Math.round(currentY*scaledHeight-mouseY);
            enableEdit = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        enableEdit = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0,0, scaledWidth, scaledHeight, 0xA0000000);
        drawPing(currentX, currentY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch(button.id){
            case 0:
                mc.displayGuiScreen(parentGui);
                break;
            case 1:
                Main.customX = currentX = 0;
                Main.customY = currentY = 0;
        }
        super.actionPerformed(button);
    }

    public void drawPing(float x, float y){
        int scaledX = Math.round(x*scaledWidth);
        int scaledY = Math.round(y*scaledHeight);
        GL11.glPushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glColor4f((float)Main.redValBg/255, (float)Main.greenValBg/255,(float)Main.blueValBg/255, Main.alphaValBg);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2i(scaledX-(int)(5*Main.scalar), scaledY-(int)(5*Main.scalar));
            GL11.glVertex2i(scaledX-(int)(5*Main.scalar), scaledY+(int)((mc.fontRendererObj.FONT_HEIGHT+5)*Main.scalar));
            GL11.glVertex2i(scaledX+(int)((mc.fontRendererObj.getStringWidth(Main.displayText.replaceAll("(\\$\\[ping])", "999"))+5)*Main.scalar), scaledY+(int)((mc.fontRendererObj.FONT_HEIGHT+5)*Main.scalar));
            GL11.glVertex2i(scaledX+(int)((mc.fontRendererObj.getStringWidth(Main.displayText.replaceAll("(\\$\\[ping])", "999"))+5)*Main.scalar), scaledY-(int)(5*Main.scalar));

        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.scale(Main.scalar, Main.scalar, Main.scalar);
        mc.fontRendererObj.drawString(Main.displayText.replaceAll("(\\$\\[ping])", String.valueOf(Events.latency)), (int)(scaledX/Main.scalar), (int)(scaledY/Main.scalar), new Color(Main.redValText, Main.greenValText, Main.blueValText).getRGB(), Main.enableTextShadow);
        GL11.glPopMatrix();
    }
}
