package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class ColorSettingsGui extends GuiScreen {
    Minecraft mc = Minecraft.getMinecraft();
    GuiScreen parentScreen;
    ColorSettingsGui(GuiScreen parentScreen){
        this.parentScreen = parentScreen;
    }
    int scaledWidth = new ScaledResolution(mc).getScaledWidth();
    int scaledHeight = new ScaledResolution(mc).getScaledHeight();
    //background
    private final GuiSlider sliderRedBg = new GuiSlider(4, (scaledWidth/2)-180, (scaledHeight/2)-125,256, 20, "Red: ", "", 0, 255, Main.redValBg, false, true);
    private final GuiSlider sliderGreenBg = new GuiSlider(5, (scaledWidth/2)-180, (scaledHeight/2)-100,256, 20, "Green: ", "", 0, 255, Main.greenValBg, false, true);
    private final GuiSlider sliderBlueBg = new GuiSlider(6, (scaledWidth/2)-180, (scaledHeight/2)-75,256, 20, "Blue: ", "", 0, 255, Main.blueValBg, false, true);
    private final GuiSlider sliderAlphaBg = new GuiSlider(7, (scaledWidth/2)-180, (scaledHeight/2)-50,256, 20, "Opacity: ", "%", 0, 100, Main.alphaValBg*100, false, true);
    //color
    private final GuiSlider sliderRedText = new GuiSlider(1, (scaledWidth/2)-180, (scaledHeight/2)-5,256, 20, "Red: ", "", 0, 255, Main.redValText, false, true);
    private final GuiSlider sliderGreenText = new GuiSlider(2, (scaledWidth/2)-180, (scaledHeight/2)+20,256, 20, "Green: ", "", 0, 255, Main.greenValText, false, true);
    private final GuiSlider sliderBlueText = new GuiSlider(3, (scaledWidth/2)-180, (scaledHeight/2)+45,256, 20, "Blue: ", "", 0, 255, Main.blueValText, false, true);
    //text shadow
    private final GuiButton toggleTextShadow = new GuiButton(8, (scaledWidth/2)-180, (scaledHeight/2)+70, mc.fontRendererObj.getStringWidth("Toggle Shadow")+60, 20, "Toggle Shadow");

    @Override
    public void initGui() {
        super.initGui();
        int stringSize = mc.fontRendererObj.getStringWidth("Back");
        buttonList.add(new GuiButton(0,(scaledWidth/2)-(stringSize/2)-30, (scaledHeight/2)+100, stringSize+60, 20, "Back"));
        buttonList.add(sliderRedText);
        buttonList.add(sliderGreenText);
        buttonList.add(sliderBlueText);
        buttonList.add(sliderRedBg);
        buttonList.add(sliderGreenBg);
        buttonList.add(sliderBlueBg);
        buttonList.add(sliderAlphaBg);
        buttonList.add(toggleTextShadow);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect((scaledWidth/2)-200,(scaledHeight/2)-150, (scaledWidth/2)+200, (scaledHeight/2)+130, 0xA0000000);
        drawString(mc.fontRendererObj,"Text:", (scaledWidth/2)-180, (scaledHeight/2)-15, 0xFFFFFF);
        drawString(mc.fontRendererObj,"Background:", (scaledWidth/2)-180, (scaledHeight/2)-135, 0xFFFFFF);
        String enableShadowText = Main.enableTextShadow ? "Enabled" : "Disabled";
        drawString(mc.fontRendererObj, enableShadowText, (scaledWidth/2)-160+buttonList.get(8).width, (scaledHeight/2)+75, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        //text
        Main.redValText = sliderRedText.getValueInt();
        Main.greenValText = sliderGreenText.getValueInt();
        Main.blueValText = sliderBlueText.getValueInt();
        //background
        Main.redValBg = sliderRedBg.getValueInt();
        Main.greenValBg = sliderGreenBg.getValueInt();
        Main.blueValBg = sliderBlueBg.getValueInt();
        Main.alphaValBg = (float)sliderAlphaBg.getValue()/100;
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.id == 0){mc.displayGuiScreen(parentScreen);}
        else if(button.id == 8){Main.enableTextShadow = !Main.enableTextShadow;}
    }

    @Override
    public void onGuiClosed() {
        new UpdateConfigs().updateGui();
        super.onGuiClosed();
    }
}
