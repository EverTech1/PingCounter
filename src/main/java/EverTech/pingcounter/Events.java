package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

@SideOnly(Side.CLIENT)
public class Events {
    Minecraft mc = Minecraft.getMinecraft();
    public static long latency = 0;
    private GuiButton button;
    private boolean updateCheck = false;
    @SubscribeEvent
    public void onTick(TickEvent e){
        if(!updateCheck){
            if(mc.theWorld!=null&&mc.thePlayer!=null){
                checkVersion();
            }
        }
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
    public void checkVersion(){
        ForgeVersion.CheckResult res = ForgeVersion.getResult(Loader.instance().activeModContainer());
        if(res.status == ForgeVersion.Status.PENDING){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    checkVersion();
                }
            }, 5000);
        }
        else{
            if(res.status == ForgeVersion.Status.OUTDATED && !updateCheck){
                mc.thePlayer.addChatComponentMessage(new ChatComponentText("Ping counter mod is outdated, please update to latest version."));
                mc.thePlayer.addChatComponentMessage(ForgeHooks.newChatWithLinks("https://www.curseforge.com/minecraft/mc-mods/pingcounter"));
            }
            updateCheck = true;
        }
    }
}