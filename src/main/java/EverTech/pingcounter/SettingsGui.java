package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class SettingsGui extends GuiScreen {
    public Minecraft mc = Minecraft.getMinecraft();
    final private boolean fromKeybind;
    public SettingsGui(boolean fromKeybind){
        this.fromKeybind = fromKeybind;
    }
    int scaledWidth = new ScaledResolution(mc).getScaledWidth();
    int scaledHeight = new ScaledResolution(mc).getScaledHeight();
    private final int size = Minecraft.getMinecraft().fontRendererObj.getStringWidth("Ping: ms ");
    private final int numSize = Minecraft.getMinecraft().fontRendererObj.getStringWidth("999");
    private final String[][] strings = {
            {"Top Left", "10", "10"}, {"Top Center",Integer.toString((scaledWidth/2)-size),"10"}, {"Top Right", Integer.toString((scaledWidth-size)-10-numSize), "10"},
            {"Middle Left","10" ,Integer.toString(scaledHeight/2) }, {"Middle Center",Integer.toString((scaledWidth/2)-size), Integer.toString(scaledHeight/2)}, {"Middle Right",Integer.toString(scaledWidth-size-10-numSize) ,Integer.toString(scaledHeight/2) },
            {"Bottom Left","10", Integer.toString(scaledHeight-10)}, {"Bottom Center",Integer.toString((scaledWidth/2)-size),Integer.toString(scaledHeight-10) }, {"Bottom Right",Integer.toString(scaledWidth-size-10-numSize), Integer.toString(scaledHeight-10)},
            {"Custom", String.valueOf(Main.customX), String.valueOf(Main.customY)}
    };
    private final GuiSlider scalarSlider = new GuiSlider(5, (scaledWidth/2)-180, (scaledHeight/2)+20,256, 20, "Scale: ", "", 0.1, 3, Main.scalar, true, true);

    @Override
    public void initGui() {
        super.initGui();
        mc.updateDisplay();
        int stringSize = mc.fontRendererObj.getStringWidth("Back");
        buttonList.add(new GuiButton(0,(scaledWidth/2)-(stringSize/2)-30, (scaledHeight/2)+70, stringSize+60, 20, "Back"));
        buttonList.add(new GuiButton(1, (scaledWidth/2)-180, (scaledHeight/2)-70, mc.fontRendererObj.getStringWidth("Toggle")+20, 20, "Toggle"));
        buttonList.add(new GuiButton(2, (scaledWidth/2)-180, (scaledHeight/2)-40, mc.fontRendererObj.getStringWidth("Change Position")+20, 20, "Change Position"));
        buttonList.add(new GuiButton(3, (scaledWidth/2)-180, (scaledHeight/2)-10,mc.fontRendererObj.getStringWidth("Color Settings")+20, 20, "Color Settings"));
        buttonList.add(new GuiButton(4, (scaledWidth/2)+30, (scaledHeight/2)-40, mc.fontRendererObj.getStringWidth("Edit Position")+20, 20, "Edit Position"));
        buttonList.add(scalarSlider);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect((scaledWidth/2)-200,(scaledHeight/2)-100, (scaledWidth/2)+200, (scaledHeight/2)+100, 0xA0000000);
        buttonList.get(4).visible = Main.selection == 9;
        drawString(mc.fontRendererObj, "Ping Counter settings", (scaledWidth/2)-190, (scaledHeight/2)-90, 0xFFFFFF);
        String toWrite = Main.enableDisplay ? "Enabled" : "Disabled";
        drawString(mc.fontRendererObj, toWrite, (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Toggle"), (scaledHeight/2)-65, 0xFFFFFF);
        drawString(mc.fontRendererObj, strings[Main.selection][0], (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Change Position"), (scaledHeight/2)-35, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        new UpdateConfigs().updatePos();
        super.onGuiClosed();
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch(button.id){
            case 0:
                if (fromKeybind) {
                    mc.thePlayer.closeScreen();
                } else {
                    mc.displayGuiScreen(new GuiIngameMenu());
                }
                break;
            case 1:
                Main.enableDisplay = !Main.enableDisplay;
                break;
            case 2:
                Main.selection = (Main.selection + 1)%10;
                break;
            case 3:
                mc.displayGuiScreen(new ColorSettingsGui(this));
                break;
            case 4:
                mc.displayGuiScreen(new PositionGui(this));
                break;
        }
    }

    @Override
    protected void mouseClickMove(int p_mouseClickMove_1_, int p_mouseClickMove_2_, int p_mouseClickMove_3_, long p_mouseClickMove_4_) {
        Main.scalar = Math.floor(scalarSlider.getValue()*10)/10;
        scalarSlider.setValue(Main.scalar);
        scalarSlider.updateSlider();
        super.mouseClickMove(p_mouseClickMove_1_, p_mouseClickMove_2_, p_mouseClickMove_3_, p_mouseClickMove_4_);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Main.scalar = Math.floor(scalarSlider.getValue()*10)/10;
        scalarSlider.setValue(Main.scalar);
        scalarSlider.updateSlider();
    }
}

