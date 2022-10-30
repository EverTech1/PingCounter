package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class Events {
    Minecraft mc = Minecraft.getMinecraft();
    public static long latency = 0;
    private GuiButton button;
    private long ticksActive = 0;
    private long lastOp = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ticksActive ++;
        if(Minecraft.getMinecraft().theWorld != null){printPing();}
    }
    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event){
        if(event.gui instanceof GuiIngameMenu && !Main.hideSettingsButton){
            button = new GuiButton(510, 15,15, mc.fontRendererObj.getStringWidth("Ping Counter Options")+10,20,"Ping Counter Options");
            event.buttonList.add(button);
        }
    }
    @SubscribeEvent
    public void buttonAction(GuiScreenEvent.InitGuiEvent.Post.ActionPerformedEvent event){
        if(event.button == this.button){
            mc.displayGuiScreen(new SettingsGui(false));
        }
    }
    @SubscribeEvent
    public void onRenderOverlayTextEvent(RenderGameOverlayEvent.Text event){
        if(Main.enableDisplay){
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GL11.glColor4f((float)Main.redValBg/255, (float)Main.greenValBg/255,(float)Main.blueValBg/255, Main.alphaValBg);
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2i(Main.positionX-5, Main.positionY-5);
                GL11.glVertex2i(Main.positionX-5, Main.positionY+mc.fontRendererObj.FONT_HEIGHT+4);
                GL11.glVertex2i(Main.positionX+mc.fontRendererObj.getStringWidth("Ping: 999 ms")+5, Main.positionY+mc.fontRendererObj.FONT_HEIGHT+4);
                GL11.glVertex2i(Main.positionX+mc.fontRendererObj.getStringWidth("Ping: 999 ms")+5, Main.positionY-5);

            }
            GL11.glEnd();
            GlStateManager.enableTexture2D();
            mc.fontRendererObj.drawStringWithShadow("Ping: "+latency+" ms", Main.positionX, Main.positionY, new Color(Main.redValText, Main.greenValText, Main.blueValText).getRGB());
        }
    }
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event){
        if(Main.openSettings.isPressed()){
            mc.displayGuiScreen(new SettingsGui(true));
        }
    }
    public void printPing()  {
        if(ticksActive-lastOp>=300){
            lastOp = ticksActive;
            try{
                EntityPlayerSP p = mc.thePlayer;
                if(p!=null) {
                    if(mc.getIntegratedServer()==null) {
                        new PingServer(mc.getCurrentServerData());
                    }
                    else{
                        latency = 0;
                    }
                }
            }
            catch(Throwable err){
                err.printStackTrace();
            }
        }
    }
}