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

import static EverTech.pingcounter.Main.scalar;

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
            drawPing();
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
    public void drawPing(){
        ScaledResolution scaled = new ScaledResolution(mc);
        int scaledHeight = scaled.getScaledHeight();
        int scaledWidth = scaled.getScaledWidth();
        final int size = Minecraft.getMinecraft().fontRendererObj.getStringWidth(Main.displayText.replaceAll("(\\$\\[ping])", ""));
        final int numSize = Minecraft.getMinecraft().fontRendererObj.getStringWidth("999")*(Main.displayText.split("(\\$\\[ping])", -1).length-1);
        final int[][] nums = {
                {10, 10}, {(scaledWidth/2)-size,10}, {scaledWidth-size-numSize-10, 10},
                {10 ,scaledHeight/2}, {(scaledWidth/2)-size, scaledHeight/2}, {scaledWidth-size-numSize-10 ,scaledHeight/2},
                {10, scaledHeight-10-mc.fontRendererObj.FONT_HEIGHT}, {(scaledWidth/2)-size,scaledHeight-10-mc.fontRendererObj.FONT_HEIGHT}, {scaledWidth-size-numSize-10, scaledHeight-10-mc.fontRendererObj.FONT_HEIGHT},
                {Math.round(Main.customX*scaledWidth), Math.round(Main.customY*scaledHeight)}
        };
        int sel = Main.selection;
        String modifiedDisplayString = Main.displayText.replaceAll("(\\$\\[ping])", String.valueOf(latency));
        GL11.glPushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GL11.glColor4f((float)Main.redValBg/255, (float)Main.greenValBg/255,(float)Main.blueValBg/255, Main.alphaValBg);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2i(nums[sel][0]-(int)(5*scalar), nums[sel][1]-(int)(5* scalar));
            GL11.glVertex2i(nums[sel][0]-(int)(5*scalar), nums[sel][1]+(int)((mc.fontRendererObj.FONT_HEIGHT+4)*scalar));
            GL11.glVertex2i(nums[sel][0]+(int)((size+numSize+5)*scalar), nums[sel][1]+(int)((mc.fontRendererObj.FONT_HEIGHT+4)*scalar));
            GL11.glVertex2i(nums[sel][0]+(int)((size+numSize+5)*scalar), nums[sel][1]-(int)(5*scalar));

        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
        GlStateManager.scale(scalar,scalar,scalar);
        mc.fontRendererObj.drawString(modifiedDisplayString, (int)(nums[sel][0]/scalar), (int)(nums[sel][1]/scalar), new Color(Main.redValText, Main.greenValText, Main.blueValText).getRGB(), Main.enableTextShadow);
        GL11.glPopMatrix();
    }
}