package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class Events {
    Minecraft mc = Minecraft.getMinecraft();
    int latency = 0;
    private GuiButton button;
    private long ticksActive = 0;
    private long lastOp = 0;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ticksActive ++;
        if(Minecraft.getMinecraft().theWorld != null){printPing();}
    }
    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.InitGuiEvent.Post event){
        if(event.gui instanceof GuiIngameMenu){
            button = new GuiButton(510, 15,15, mc.fontRendererObj.getStringWidth("Ping Counter Options")+10,20,"Ping Counter Options");
            event.buttonList.add(button);
        }
    }
    @SubscribeEvent
    public void buttonAction(GuiScreenEvent.InitGuiEvent.Post.ActionPerformedEvent event){
        if(event.button == this.button){
            mc.displayGuiScreen(new SettingsGui());
        }
    }
    @SubscribeEvent
    public void onRenderTextEvent(RenderGameOverlayEvent.Text event){
        if(Main.enableDisplay){
            mc.fontRendererObj.drawStringWithShadow("Ping: "+latency+" ms", Main.positionX, Main.positionY, new Color(Main.redVal, Main.greenVal, Main.blueVal).getRGB());
        }
    }
    public void printPing()  {
        if(ticksActive-lastOp>120){
            lastOp = ticksActive;
            try{
                EntityPlayerSP p = mc.thePlayer;
                if(p!=null) {
                    latency = mc.getNetHandler().getPlayerInfo(p.getUniqueID()).getResponseTime();
                }
            }
            catch(Throwable err){
                err.printStackTrace();
            }
        }
    }
}
