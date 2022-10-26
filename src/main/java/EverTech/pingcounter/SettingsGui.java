package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class SettingsGui extends GuiScreen {
    public static int scaledWidth = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
    public static int scaledHeight = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
    private final int size = Minecraft.getMinecraft().fontRendererObj.getStringWidth("Ping: ms");
    private final int numSize = Minecraft.getMinecraft().fontRendererObj.getStringWidth("999");
    private final String[][] strings = {{"Top Left", "10", "10"}, {"Top Center",Integer.toString((scaledWidth/2)-size),"10"}, {"Top Right", Integer.toString((scaledWidth-size)-10-numSize), "10"}, {"Middle Left","10" ,Integer.toString(scaledHeight/2) }, {"Middle Center",Integer.toString((scaledWidth/2)-size), Integer.toString(scaledHeight/2)}, {"Middle Right",Integer.toString(scaledWidth-size-10-numSize) ,Integer.toString(scaledHeight/2) }, {"Bottom Left","10", Integer.toString(scaledHeight-10)}, {"Bottom Center",Integer.toString((scaledWidth/2)-size),Integer.toString(scaledHeight-10) }, {"Bottom Right",Integer.toString(scaledWidth-size-10-numSize), Integer.toString(scaledHeight-10) }};
    public GuiSlider sliderRed = new GuiSlider(3, (scaledWidth/2)-180, (scaledHeight/2)-10,256, 20, "Red: ", "", 0, 255, Main.redVal, false, true);
    public GuiSlider sliderGreen = new GuiSlider(4, (scaledWidth/2)-180, (scaledHeight/2)+15,256, 20, "Green: ", "", 0, 255, Main.greenVal, false, true);
    public GuiSlider sliderBlue = new GuiSlider(5, (scaledWidth/2)-180, (scaledHeight/2)+40,256, 20, "Blue: ", "", 0, 255, Main.blueVal, false, true);

    @Override
    public void initGui() {
        super.initGui();
        int stringSize = mc.fontRendererObj.getStringWidth("Back");
        buttonList.add(new GuiButton(0,(scaledWidth/2)-(stringSize/2)-30, (scaledHeight/2)+70, stringSize+60, 20, "Back"));
        buttonList.add(new GuiButton(1, (scaledWidth/2)-180, (scaledHeight/2)-70, mc.fontRendererObj.getStringWidth("Toggle")+20, 20, "Toggle"));
        buttonList.add(new GuiButton(2, (scaledWidth/2)-180, (scaledHeight/2)-40, mc.fontRendererObj.getStringWidth("Change Position")+20, 20, "Change Position"));
        buttonList.add(sliderRed);
        buttonList.add(sliderGreen);
        buttonList.add(sliderBlue);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect((scaledWidth/2)-200,(scaledHeight/2)-100, (scaledWidth/2)+200, (scaledHeight/2)+100, 0xA0000000);
        drawString(mc.fontRendererObj, "Ping Counter settings", (scaledWidth/2)-190, (scaledHeight/2)-90, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        String toWrite = Main.enableDisplay ? "Enabled" : "Disabled";
        drawString(mc.fontRendererObj, toWrite, (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Toggle"), (scaledHeight/2)-65, 0xFFFFFF);
        drawString(mc.fontRendererObj, strings[Main.selection][0], (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Change Position"), (scaledHeight/2)-35, 0xFFFFFF);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Main.redVal = sliderRed.getValueInt();
        Main.greenVal = sliderGreen.getValueInt();
        Main.blueVal = sliderBlue.getValueInt();
        new UpdateConfigs().update(Main.positionX, Main.positionY, Main.selection, Main.enableDisplay, Main.redVal, Main.greenVal, Main.blueVal);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch(button.id){
            case 0:
                mc.displayGuiScreen(new GuiIngameMenu());
                break;
            case 1:
                Main.enableDisplay = !Main.enableDisplay;
                break;
            case 2:
                Main.selection = (Main.selection + 1)%9;
                String[] selected = strings[Main.selection];
                int x = Integer.parseInt(selected[1]);
                int y = Integer.parseInt(selected[2]);
                Main.positionX = x;
                Main.positionY = y;
                break;
        }
    }
}
