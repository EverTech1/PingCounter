package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class Events {
    Minecraft mc = Minecraft.getMinecraft();
    long latency = 0;
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
    public void onRenderTextEvent(RenderGameOverlayEvent.Text event){
        if(Main.enableDisplay){
            mc.fontRendererObj.drawStringWithShadow("Ping: "+latency+" ms", Main.positionX, Main.positionY, new Color(Main.redVal, Main.greenVal, Main.blueVal).getRGB());
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
                        long pts = mc.getCurrentServerData().pingToServer;
                        new GuiMultiplayer(mc.currentScreen).getOldServerPinger().ping(mc.getCurrentServerData());
                        latency = pts;
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