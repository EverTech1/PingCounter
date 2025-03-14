package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class SettingsGui extends GuiScreen {

    private Minecraft mc = Minecraft.getMinecraft();

    private final ScaledResolution res = new ScaledResolution(mc);
    private final int scaledWidth = res.getScaledWidth();
    private final int scaledHeight = res.getScaledHeight();

    private final int boxWidth = 400;
    private final int boxHeight = 230;
    private final int boxCornerX = (scaledWidth/2)-(boxWidth/2);
    private final int boxCornerY = (scaledHeight/2)-(boxHeight/2);

    private final boolean fromKeybind;

    public SettingsGui(boolean fromKeybind){
        this.fromKeybind = fromKeybind;
    }
    private final int backStringSize = mc.fontRendererObj.getStringWidth("Back");
    private final GuiButton backButton = new GuiButton(0,(scaledWidth/2)-(backStringSize/2)-30, boxCornerY+boxHeight-30, backStringSize+60, 20, "Back") ;
    private final GuiButton toggleButton = new GuiButton(1, boxCornerX+20, boxCornerY+30, mc.fontRendererObj.getStringWidth("Disabled")+20, 20, Main.enableDisplay ? "Enabled" : "Disabled");
    private final GuiButton colorSettingsButton = new GuiButton(2, boxCornerX+20, boxCornerY+60,mc.fontRendererObj.getStringWidth("Color Settings")+20, 20, "Color Settings");
    private final GuiButton positionSettingsButton = new GuiButton(3, boxCornerX+20, boxCornerY+90, mc.fontRendererObj.getStringWidth("Edit Position")+20, 20, "Edit Position");
    private final GuiSlider scalarSlider = new GuiSlider(5, boxCornerX+20, boxCornerY+120,256, 20, "Scale: ", "", 0.1, 3, Main.scalar, true, true);
    private final GuiTextField customText = new GuiTextField(6, mc.fontRendererObj, boxCornerX+20, boxCornerY+150, 256, 20);
    private final GuiButton resetTextButton = new GuiButton(7, boxCornerX+290, boxCornerY+150, mc.fontRendererObj.getStringWidth("Reset")+60, 20, "Reset");

    @Override
    public void initGui() {
        super.initGui();
        System.out.println(mc.fontRendererObj.getStringWidth("Reset")+60);
        mc.updateDisplay();
        buttonList.add(backButton);
        buttonList.add(toggleButton);
        buttonList.add(colorSettingsButton);
        buttonList.add(positionSettingsButton);
        buttonList.add(resetTextButton);
        buttonList.add(scalarSlider);
        customText.setText(Main.displayText);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(boxCornerX,boxCornerY, boxCornerX+boxWidth, boxCornerY+boxHeight, 0xA0000000);
        drawString(mc.fontRendererObj, "Ping Counter settings", boxCornerX+20, boxCornerY+10, 0xFFFFFF);
        customText.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        customText.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public void onGuiClosed() {
        Main.displayText = !customText.getText().isEmpty() ? customText.getText() : "Ping $[ping]ms";
        new UpdateConfigs().updatePos();
        new UpdateConfigs().updateGui();
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
                toggleButton.displayString = Main.enableDisplay ? "Enabled" : "Disabled";
                break;
            case 2:
                mc.displayGuiScreen(new ColorSettingsGui(this));
                break;
            case 3:
                mc.displayGuiScreen(new PositionGui(this));
                break;
            case 7:
                customText.setText("Ping: $[ping]ms");
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        customText.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        customText.textboxKeyTyped(typedChar, keyCode);
        Main.displayText = customText.getText();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Main.scalar = Math.floor(scalarSlider.getValue()*10)/10;
        scalarSlider.setValue(Main.scalar);
        scalarSlider.updateSlider();
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        Main.scalar = Math.floor(scalarSlider.getValue()*10)/10;
        scalarSlider.setValue(Main.scalar);
        scalarSlider.updateSlider();
    }
}

